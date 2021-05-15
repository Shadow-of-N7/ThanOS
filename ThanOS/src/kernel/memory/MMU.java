package kernel.memory;

import rte.DynamicRuntime;

public class MMU {
    // Directory starts at this address, directly followed by the tables
    private static int _baseAddress;
    private static int _currentAddress;

    // https://www.youtube.com/watch?v=59rEMnKWoS4


    /**
     * Initializes the MMU. Needs to be called before advanced memory mode is activated.
     */
    public static void initialize() {
        // Build page dir and tables
        _baseAddress = DynamicRuntime.allocateSpecialMemory(4096 + 1024 * 4096, 4096);
        _currentAddress = _baseAddress;
    }


    public static void setCR3(int addr) {
        MAGIC.inline(0x8B, 0x45); MAGIC.inlineOffset(1, addr); //mov eax,[ebp+8]
        MAGIC.inline(0x0F, 0x22, 0xD8); //mov cr3,eax
    }


    public static void enableVirtualMemory() {
        MAGIC.inline(0x0F, 0x20, 0xC0); //mov eax,cr0
        MAGIC.inline(0x0D, 0x00, 0x00, 0x01, 0x80); //or eax,0x80010000
        MAGIC.inline(0x0F, 0x22, 0xC0); //mov cr0,eax
    }


    public static int getCR2() {
        int cr2=0;
        MAGIC.inline(0x0F, 0x20, 0xD0); //mov e/rax,cr2
        MAGIC.inline(0x89, 0x45); MAGIC.inlineOffset(1, cr2); //mov [ebp-4],eax
        return cr2;
    }


    private static void buildStructure() {

        for(int i = 0; i < 1024; i++) {
            //buildPageDirectoryEntry(); // Align to 4096 bytes
        }

        for(int i = 0; i < 1024; i++) {
            buildPageTable(i);
        }

        // TODO: Set DynamicRuntime next basic address to the address after the dir and tables so it doesn't get overwritten
    }


    /**
     * Creates a page directory entry. Should point at one page table.
     * @param targetAddress Which address the entry shall point at.
     */
    private static void buildPageDirectoryEntry(int targetAddress) {
        int value = 3;
        // TODO: Get address of a page table
    }


    /**
     * Builds a page table entry.
     * @param offset The table number, so not every table points at the same pages.
     */
    private static void buildPageTable(int offset) {
        for(int i = 0; i < 1024; i++) {
            buildPageTableEntry(4096 * i * offset);
        }
    }


    /**
     * Creates a page table entry.
     * @param targetAddress Which address the entry shall point at.
     */
    private static void buildPageTableEntry(int targetAddress) {
        // Set lower bits - Writable and Present
        int value = 0x3;
        value |= (targetAddress << 12);
        MAGIC.wMem32(value, _currentAddress);
        _currentAddress += 4;
    }
}
