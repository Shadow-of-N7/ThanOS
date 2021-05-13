package kernel.memory;

import collections.MemoryBlockList;
import devices.StaticV24;
import kernel.BlueScreen;
import rte.DynamicRuntime;

public class Memory {
    private static Object _lastEmptyObject = null;
    private static Object _firstEmptyObject = null;
    private static Object _firstHeapObject = null;
    private static Object _lastHeapObject = null;
    private static MemoryBlockList map;
    private static int _sjcImageSize;
    private static int _sjcImageUpperAddress;


    public static boolean isAdvancedMode = false;

    public static void initialize() {
        // Create Object List
        _sjcImageSize = MAGIC.rMem32(MAGIC.imageBase + 4);
        _sjcImageUpperAddress = MAGIC.imageBase + _sjcImageSize;
        DynamicRuntime.initializeFreeAddresses();
        map = MemoryMap.getMemoryMap();

        initializeEmptyObjects();
        // Set this to false to stay in basic mode - works too.
        isAdvancedMode = true;
        GC.initialize();
    }


    /**
     * Creates initial EmptyObjects in free memory areas above the base image.
     */
    public static void initializeEmptyObjects() {
        // Get first available mem address - always above the system image and basic mode stuff
        int basicSize = DynamicRuntime.getBasicNextAddress();

        for(int i = 0; i < map.getLength(); i++) {
            MemoryBlock block = map.elementAt(i);
            if(block.blockType == MemoryBlock.BlockType.Free
                    && block.baseAddress + basicSize >= (MAGIC.imageBase + basicSize)) {
                if(block.baseAddress < basicSize) {
                    addEmptyObject(basicSize, (int)(block.baseAddress + block.blockLength) - basicSize);
                }
                else {
                    addEmptyObject((int) block.baseAddress, (int) block.blockLength);
                }
                return;
            }
        }
    }


    /**
     * Returns a free address for a memory block of the given size.
     * @param size
     * @return
     */
    public static int getFreeAddress(int size) {
        // Loop through the empty objects and find one big enough for the requested size.
        Object currentEmptyObject = _firstEmptyObject;
        int requiredSize = (size + 3) & ~3;
        int emptyObjectMinSize = (getMinimalEmptyObjectSize() + 3) & ~3;

        int freeAddress = -1;

        // Find the highest free object
         do {
             int emptyObjectSize = currentEmptyObject._r_scalarSize + (currentEmptyObject._r_relocEntries << 2);

             if(emptyObjectSize >= requiredSize) {
                 // Object fits with some empty object remaining
                 if(requiredSize <= emptyObjectSize - emptyObjectMinSize + 9 /*Adapt to size when marked is used*/) {
                     shrinkEmptyObject(currentEmptyObject, size);
                     freeAddress = MAGIC.cast2Ref(currentEmptyObject) + currentEmptyObject._r_scalarSize;
                 }
                 // Not enough space left for a complete empty object
                 else {
                    freeAddress = getObjectLowerAddress(currentEmptyObject);
                    DynamicRuntime.sizeOffset = emptyObjectSize - requiredSize;
                    removeEmptyObject(currentEmptyObject);
                 }
                 break;
             }
             currentEmptyObject = currentEmptyObject._r_next;

        } while ((currentEmptyObject != null ? currentEmptyObject._r_next : null) != null);

        // No memory left
        if(freeAddress == -1) {
            BlueScreen.raise("out of heap memory");
            while (true){}
        }
        return freeAddress;
    }


    /**
     * Shrinks a given object by some amount.
     * @param emptyObject Which object to shrink.
     * @param amount By how much to shrink the object.
     */
    public static void shrinkEmptyObject(Object emptyObject, int amount) {
        if(emptyObject._r_scalarSize > amount) {
            MAGIC.assign(emptyObject._r_scalarSize, emptyObject._r_scalarSize - amount);
        }
    }


    /**
     * Updates the last object address.
     * @param object
     */
    public static void updateLastObject(Object object) {
        _lastHeapObject = object;
        //StaticV24.printHex(MAGIC.cast2Ref(_lastHeapObject), 8);
        //StaticV24.println();
        if(_firstHeapObject == null) {
            _firstHeapObject = object;
        }
    }

    /**
     * Returns the object of the last created object.
     * @return Object address of the last created object.
     */
    public static Object getLastObject() {
        return _lastHeapObject;
    }


    /**
     * Adds a new EmptyObject to the list.
     * @param startAddress Start address of the new EmptyObect.
     * @param size Size of the new EmptyObject.
     */
    private static void addEmptyObject(int startAddress, int size) {
        Object emptyObject = DynamicRuntime.newEmptyObject(startAddress, size);

        if(_firstEmptyObject == null) {
            _firstEmptyObject = emptyObject;
        }
        if(_lastEmptyObject != null) {
            MAGIC.assign(_lastEmptyObject._r_next, emptyObject);
        }
        _lastEmptyObject = emptyObject;
    }


    private static int getEmptyObjectCount() {
        int counter = 0;
        Object currentEmptyObject = _firstEmptyObject;
        while (currentEmptyObject != null) {
            ++counter;
            if(currentEmptyObject == currentEmptyObject._r_next) {
                return counter;
            }
            currentEmptyObject = currentEmptyObject._r_next;
        }
        return counter;
    }


    public static void printEmptyObjectInfo() {
        StaticV24.print("Empty object count: ");
        StaticV24.println(getEmptyObjectCount());
        Object currentEmptyObject = _firstEmptyObject;
        while (currentEmptyObject != null) {
            //Console.print("\t");
            StaticV24.printHex(getObjectLowerAddress(currentEmptyObject), 8);
            StaticV24.print('-');
            StaticV24.printHex(getObjectUpperAddress(currentEmptyObject), 8);
            StaticV24.println();

            if(currentEmptyObject == currentEmptyObject._r_next) {
                return;
            }
            currentEmptyObject = currentEmptyObject._r_next;
        }

    }


    private static int getMinimalEmptyObjectSize() {
        return MAGIC.getInstScalarSize("EmptyObject") + (MAGIC.getInstRelocEntries("EmptyObject") * 4);
    }


    @SJC.Inline
    private static int getObjectUpperAddress(Object object) { return MAGIC.cast2Ref(object) + object._r_scalarSize; }


    @SJC.Inline
    private static int getObjectLowerAddress(Object object) {
        return MAGIC.cast2Ref(object) - object._r_relocEntries * 4;
    }


    @SJC.Inline
    private static int getObjectSize(Object object) {
        return getObjectUpperAddress(object) - getObjectLowerAddress(object);
    }


    @SJC.Inline
    private static void removeEmptyObject(Object emptyObject) {
        MAGIC.assign(getPreviousEmptyObject(emptyObject)._r_next, emptyObject._r_next);
    }


    /**
     * Deletes an object.
     * @param object
     * @return The next object as seen by the deleted object.
     */
    public static Object removeHeapObject(Object object) {
        // Update first reference if first object is affected
        Object nextObject = object._r_next;
        if(object == _firstHeapObject) {
            _firstHeapObject = object._r_next;
        }
        else {
            Object previousObject = getPreviousHeapObject(object);
            if(object == _lastHeapObject) {
                _lastHeapObject = previousObject;
            }
            if(previousObject != null) {
                MAGIC.assign(previousObject._r_next, object._r_next);
            }
        }
        addEmptyObject(getObjectLowerAddress(object), getObjectSize(object));
        return nextObject;
    }


    /**
     * Merges neighbouring empty objects into one bigger.
     * @return How many merge operations have been executed.
     */
    public static int mergeEmptyObjects() {
        int counter = 0;
        Object currentEmptyObject = _firstEmptyObject;

        while (currentEmptyObject != null) {
            Object compareObject = _firstEmptyObject;

            while (compareObject != null) {
                if(currentEmptyObject instanceof EmptyObject
                        && compareObject instanceof EmptyObject
                        && currentEmptyObject != compareObject) {
                    int currentLowerAddress = getObjectLowerAddress(currentEmptyObject);
                    int compareLowerAddress = getObjectLowerAddress(compareObject);
                    int currentUpperAddress = getObjectUpperAddress(currentEmptyObject);
                    int compareUpperAddress = getObjectUpperAddress(compareObject);

                    // Current beneath compare
                    if (currentUpperAddress == compareLowerAddress) {
                        // If currentEmpty points to compare, we must ensure compare doesn't point to itself after
                        // the next update!
                        if(compareObject._r_next == currentEmptyObject) {
                            MAGIC.assign(getPreviousEmptyObject(compareObject)._r_next, currentEmptyObject);
                        } else {
                            MAGIC.assign(currentEmptyObject._r_next, compareObject._r_next); // Chain up
                        }
                        MAGIC.assign(currentEmptyObject._r_scalarSize, currentEmptyObject._r_scalarSize + getObjectSize(compareObject));
                        counter++;
                        break;
                    }
                    // Current above compare
                    if (currentLowerAddress == compareUpperAddress) {
                        // If currentEmpty points to compare, we must ensure compare doesn't point to itself after
                        // the next update!
                        if(currentEmptyObject._r_next == compareObject) {
                            MAGIC.assign(getPreviousEmptyObject(currentEmptyObject)._r_next, compareObject);
                        }else {
                            MAGIC.assign(compareObject._r_next, currentEmptyObject._r_next);
                        }
                        MAGIC.assign(compareObject._r_scalarSize, compareObject._r_scalarSize + getObjectSize(currentEmptyObject));
                        counter++;
                        break;
                    }
                }
                compareObject = compareObject._r_next;
            }
            currentEmptyObject = currentEmptyObject._r_next;
        }
        return counter;
    }


    private static Object getPreviousEmptyObject(Object o) {
        Object currentEmptyObject = _firstEmptyObject;
        Object result = null;
        while (currentEmptyObject._r_next != null) {
            if(currentEmptyObject._r_next == o) {
                result = currentEmptyObject;
            }
            currentEmptyObject = currentEmptyObject._r_next;
        }
        return result;
    }


    private static Object getPreviousHeapObject(Object o) {
        Object currentObject = _firstHeapObject;
        Object result = null;
        while (currentObject._r_next != null) {
            if(currentObject._r_next == o) {
                result = currentObject;
            }
            currentObject = currentObject._r_next;
        }
        return result;
    }


    @SJC.Inline
    public static Object getFirstHeapObject() {
        return _firstHeapObject;
    }


    @SJC.Inline
    public static int getSjcUpperAddress() {
        return _sjcImageUpperAddress;
    }


    public static int getHeapObjectCount() {
        int counter = 0;
        Object currentObject = _firstHeapObject;
        while (currentObject != _lastHeapObject) {
            counter++;
            currentObject = currentObject._r_next;
        }
        return counter;
    }
}
