package kernel.memory;

import collections.MemoryBlockList;
import kernel.BlueScreen;
import rte.DynamicRuntime;

public class Memory {
    private static Object _lastEmptyObject = null;
    private static Object _firstEmptyObject = null;
    private static Object _firstHeapObject = null;
    private static Object _lastHeapObject = null;
    private static MemoryBlockList map;

    public static boolean isAdvancedMode = false;

    public static void initialize() {
        // Create Object List
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
                 if(requiredSize <= emptyObjectSize - emptyObjectMinSize + 8 /*Adapt to size when marked is used*/) {
                     shrinkEmptyObject(currentEmptyObject, size);
                     freeAddress = MAGIC.cast2Ref(currentEmptyObject) + currentEmptyObject._r_scalarSize;
                     break;
                 }
                 // Not enough space left for a complete empty object
                 else {
                    freeAddress = getObjectLowerAddress(currentEmptyObject);
                    DynamicRuntime.sizeOffset = emptyObjectSize - requiredSize;
                    removeEmptyObject(currentEmptyObject);
                    break;
                 }
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


    private static int getMinimalEmptyObjectSize() {
        return MAGIC.getInstScalarSize("EmptyObject") + MAGIC.getInstRelocEntries("EmptyObject") * 4;
    }


    private static int getObjectUpperAddress(Object o) { return MAGIC.cast2Ref(o) + o._r_scalarSize; }


    private static int getObjectLowerAddress(Object o) {
        return MAGIC.cast2Ref(o) - o._r_relocEntries * 4;
    }


    private static int getObjectSize(Object o) { return getObjectUpperAddress(o) - getObjectLowerAddress(o); }


    private static void removeEmptyObject(Object emptyObject) {
        MAGIC.assign(getPreviousEmptyObject(emptyObject)._r_next, emptyObject._r_next);
    }


    public static void removeHeapObject(Object object) {
        // Update first reference if first object is affected
        if(object == _firstHeapObject) {
            _firstHeapObject = object._r_next;

        }
        else {
            Object previousObject = getPreviousHeapObject(object);
            MAGIC.assign(previousObject._r_next, object._r_next);
        }
        addEmptyObject(getObjectLowerAddress(object), getObjectSize(object));
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
}
