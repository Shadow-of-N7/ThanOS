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
}
