package kernel.memory;

public class MemoryBlock {
    long baseAddress;
    long blockLength;
    int blockType;

    public MemoryBlock(long baseAddress, long blockLength, int blockType) {
        this.baseAddress = baseAddress;
        this.blockLength = blockLength;
        this.blockType = blockType;
    }


    public static class BlockType {
        public static final int Free = 1;
        public static final int Reserved = 2;
        public static final int AcpiReclaimable = 3;
        public static final int AcpiNVS = 4;
        public static final int BadMemory = 5;
    }
}
