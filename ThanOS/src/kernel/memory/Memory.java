package kernel.memory;

import collections.MemoryBlockList;
import collections.ObjectList;
import io.Console;
import rte.DynamicRuntime;

public class Memory {
    private static ObjectList _imageObjects;
    private static ObjectList _emptyObjects;
    private static ObjectList _heapObjects;
    private static int _lastObjectAddress = -1;

    public static void initialize() {
        // Create Object List
        _imageObjects = new ObjectList();
        _emptyObjects = new ObjectList();
        _heapObjects = new ObjectList();
    }


    public static void setEmptyObjects() {
        MemoryBlockList map = MemoryMap.getMemoryMap();
        int imageSize = MAGIC.rMem32(MAGIC.imageBase + 4);

        for(int i = 0; i < map.getLength(); i++) {
            MemoryBlock block = map.elementAt(i);
            if(block.blockType == MemoryBlock.BlockType.Free
                    && block.baseAddress + imageSize >= (MAGIC.imageBase + imageSize)) {
                addEmptyObject((int)block.baseAddress, (int)block.blockLength);
                Console.println();
                Console.print("Created empty object with size: ");
                Console.println(_emptyObjects.elementAt(0)._r_relocEntries + _emptyObjects.elementAt(0)._r_scalarSize);
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
        for(int i = _emptyObjects.getLength(); i > 0; i--) {
            Object emptyObject = _emptyObjects.elementAt(i);
            int emptyObjectSize = emptyObject._r_scalarSize +_emptyObjects._r_scalarSize;
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


    public static void updateLastObjectAddress(int address) {
        _lastObjectAddress = address;
    }

    public static int getLastObjectAddress() {
        return _lastObjectAddress;
    }


    private static void addEmptyObject(int startAddress, int size) {
        Object foo = DynamicRuntime.newEmptyObject(startAddress, size);
        _emptyObjects.add(foo);
    }
}
