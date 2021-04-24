package kernel.memory;

import collections.MemoryBlockList;
import collections.ObjectList;
import io.Console;
import rte.DynamicRuntime;

public class Memory {
    private static ObjectList _imageObjects;
    private static ObjectList _emptyObjects;
    private static ObjectList _heapObjects;
    private static MemoryBlockList map;

    // Stores the last object address for next chaining
    private static int _lastObjectAddress = -1;

    public static boolean isAdvancedMode = false;

    public static void initialize() {
        // Create Object List
        DynamicRuntime.initializeFreeAddresses();
        _imageObjects = new ObjectList();
        _emptyObjects = new ObjectList();
        _heapObjects = new ObjectList();
        map = MemoryMap.getMemoryMap();

        initializeEmptyObjects();
        isAdvancedMode = true;
    }


    /**
     * Creates initial EmptyObjects in free memory areas above the base image.
     */
    public static void initializeEmptyObjects() {

        int imageSize = MAGIC.rMem32(MAGIC.imageBase + 4);

        for(int i = 0; i < map.getLength(); i++) {
            MemoryBlock block = map.elementAt(i);
            if(block.blockType == MemoryBlock.BlockType.Free
                    && block.baseAddress + imageSize >= (MAGIC.imageBase + imageSize)) {

                addEmptyObject((int)block.baseAddress, (int)block.blockLength);
                Console.println();
                Console.print("Created empty object with size: ");
                Console.println((_emptyObjects.elementAt(0)._r_relocEntries << 2) + _emptyObjects.elementAt(0)._r_scalarSize);
                return;
            }
        }
    }


    /**
     * Returns a free address for the given size.
     * @param size
     * @return
     */
    public static int getFreeAddress(int size) {
        // Loop through the empty objects and find one big enough for the requested size.
        for(int i = _emptyObjects.getLength() - 1; i > 0; i--) {
            Object emptyObject = _emptyObjects.elementAt(i); // THIS IS IT
            int emptyObjectSize = emptyObject._r_scalarSize + (emptyObject._r_relocEntries << 2);
            if(emptyObjectSize >= size) {
                shrinkEmptyObject(emptyObject, size);
                return _emptyObjects.elementAt(i)._r_scalarSize - size;
            }
        }
        return 0;
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
     * @param address
     */
    public static void updateLastObjectAddress(int address) {
        _lastObjectAddress = address;
    }

    /**
     * Returns the object of the last created object.
     * @return Object address of the last created object.
     */
    public static int getLastObjectAddress() {
        return _lastObjectAddress;
    }


    /**
     * Adds a new EmptyObject to the list.
     * @param startAddress Start address of the new EmptyObect.
     * @param size Size of the new EmptyObject.
     */
    private static void addEmptyObject(int startAddress, int size) {
        Object foo = DynamicRuntime.newEmptyObject(startAddress, size);
        _emptyObjects.add(foo);
    }
}
