package kernel.memory;

import devices.StaticV24;
import rte.DynamicRuntime;

public class MMU {
    // Directory starts at this address, directly followed by the tables
    private static int _baseAddress;
    // Points at the current entries of page directory and tables
    private static int _currentAddress;
    // Points at the pages
    private static int _currentTargetAddress = 0;
    private static boolean _isInitialized = false;
    private static int _lastAccessedAddress;

    private static final int DIRECTORY_ENTRY_COUNT = 1024;
    private static final int DIRECTORY_SIZE = 4096;
    private static final int TABLE_COUNT = 1024;
    private static final int TABLE_SIZE = 4096;

    // https://www.youtube.com/watch?v=59rEMnKWoS4


    /**
     * Initializes the MMU. Needs to be called before advanced memory mode is activated.
     */
    public static void initialize() {
        if(!_isInitialized) {
            // Build page dir and tables
            _baseAddress = DynamicRuntime.allocateSpecialMemory(
                    DIRECTORY_SIZE + (TABLE_COUNT * TABLE_SIZE),
                    4096);
            _currentAddress = _baseAddress;
            buildStructure();
        }
        _isInitialized = true;
        enableVirtualMemory();
    }


    private static void setCR3(int addr) {
        MAGIC.inline(0x8B, 0x45); MAGIC.inlineOffset(1, addr); //mov eax,[ebp+8]
        MAGIC.inline(0x0F, 0x22, 0xD8); //mov cr3,eax
    }


    public static void enableVirtualMemory() {
        if(!_isInitialized) {
            initialize();
        }

        MAGIC.inline(0x0F, 0x20, 0xC0); //mov eax,cr0
        MAGIC.inline(0x0D, 0x00, 0x00, 0x01, 0x80); //or eax,0x80010000
        MAGIC.inline(0x0F, 0x22, 0xC0); //mov cr0,eax
    }


    private static int getCR2() {
        int cr2=0;
        MAGIC.inline(0x0F, 0x20, 0xD0); //mov e/rax,cr2
        MAGIC.inline(0x89, 0x45); MAGIC.inlineOffset(1, cr2); //mov [ebp-4],eax
        return cr2;
    }


    private static void buildStructure() {

        for(int i = 0; i < DIRECTORY_ENTRY_COUNT; i++) {
            int tableAddress = (_baseAddress + 4096) + 4096 * i;
            buildPageDirectoryEntry(tableAddress);
        }
        // Two different loops so the current
        for(int i = 0; i < DIRECTORY_ENTRY_COUNT; i++) {
            buildPageTable(i);
        }

        setCR3(_baseAddress);
    }


    /**
     * Creates a page directory entry. Should point at one page table.
     * @param targetAddress Which address the entry shall point at.
     */
    private static void buildPageDirectoryEntry(int targetAddress) {
        int value = 3;
        // No shifting required, the lower 12 bits of targetAddress are 0 because of the 4096 bit alignment
        value |= targetAddress;
        MAGIC.wMem32(_currentAddress, value);
        _currentAddress += 4;
    }


    /**
     * Builds a page table entry.
     * @param offset The table number, so not every table points at the same pages.
     */
    private static void buildPageTable(int offset) {
        int adjustedOffset = offset + 1;
        for(int i = 0; i < 1024; i++) {
            buildPageTableEntry();
        }
    }


    /**
     * Creates a page table entry.
     */
    private static void buildPageTableEntry() {
        // Set lower bits - Writable and Present
        int value = 0x3;
        // No shifting required, the lower 12 bits of targetAddress are 0 because of the 4096 bit alignment
        value |= _currentTargetAddress;
        _currentTargetAddress += 4096;
        MAGIC.wMem32(_currentAddress, value);
        _currentAddress += 4;
    }


    /**
     * Returns the last accessed address.
     * @return The last accessed address.
     */
    @SJC.Inline
    public static int getLastAccessedAddress() {
        return _lastAccessedAddress;
    }
}
