package kernel.memory;

import collections.MemoryBlockList;
import io.Console;
import kernel.BlueScreen;
import rte.DynamicRuntime;

public class Memory {
    private static Object _lastEmptyObject = null;
    private static Object _firstEmptyObject = null;
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
    }


    /**
     * Creates initial EmptyObjects in free memory areas above the base image.
     */
    public static void initializeEmptyObjects() {

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
                Console.println();
                Console.print("Created empty object with size: ");
                Console.println((_lastEmptyObject._r_relocEntries << 2) + _lastEmptyObject._r_scalarSize);
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
        Object highestFreeObject = null;
        // Find the highest free object
         do {
             int emptyObjectSize = currentEmptyObject._r_scalarSize + (currentEmptyObject._r_relocEntries << 2);
             if(emptyObjectSize > size) {
                 highestFreeObject = currentEmptyObject;
             }
             currentEmptyObject = currentEmptyObject._r_next;
        } while ((currentEmptyObject != null ? currentEmptyObject._r_next : null) != null);
        // Shrink the highest free empty object
        // No memory left
        if(highestFreeObject == null) {
            BlueScreen.raise("out of heap memory");
        }
        shrinkEmptyObject(highestFreeObject, size);
        return MAGIC.cast2Ref(highestFreeObject) + highestFreeObject._r_scalarSize;
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
}
