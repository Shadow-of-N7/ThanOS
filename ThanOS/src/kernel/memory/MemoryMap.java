package kernel.memory;

import collections.MemoryBlockList;

public class MemoryMap {
    MemoryBlockList memoryBlocks;

    public MemoryMap() {
        memoryBlocks = new MemoryBlockList();
    }
}
