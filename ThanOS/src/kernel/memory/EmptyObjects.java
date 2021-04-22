package kernel.memory;

import collections.MemoryBlockList;
import collections.ObjectList;

public class EmptyObjects {
    ObjectList freeObjects;


    public void setEmptyObjects() {
        MemoryBlockList map = MemoryMap.getMemoryMap();
        for(int i = 0; i < map.getLength(); i++) {
            MemoryBlock block = map.elementAt(i);
            if(block.blockType == MemoryBlock.BlockType.Free
                    && block.baseAddress > (MAGIC.imageBase + MAGIC.rMem32(MAGIC.imageBase + 4))) {

            }
        }
    }


    public void update() {

    }
}
