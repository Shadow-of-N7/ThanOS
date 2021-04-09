package kernel;

import io.Console;
import io.Keyboard;

public class Interrupt {
    private final static int MASTER = 0x20, SLAVE = 0xA0;

    // Put the IDT directly after the GDT and some parts of the bootloader
    // This starts at
    private final static int IDT_BASE_ADDRESS = 0x7E00;
    private final static int IDT_ENTRY_COUNT = 48;
    private static boolean _isInitialized = false;


    public static void initialize() {
        buildIDT();
        _isInitialized = true;
    }


    /**
     * Start or stop using interrupts.
     * @param start Set to true to start using interrupts; else false.
     */
    public static void useInterrupts(boolean start) {
        if(start) {
            initPic();
            // Unlock interrupts
            MAGIC.inline(0xFB);
        }
        else {

            // Lock interrupts
            MAGIC.inline(0xFA);
        }
    }


    public static void loadInterruptDescriptorTable()
    {
        if (!_isInitialized)
        {
            buildIDT();
            useInterrupts(true);
        }
        int tableLimit = IDT_ENTRY_COUNT * 8 - 1; // Byte count in table
        long tmp = (((long)IDT_BASE_ADDRESS) << 16) | (long)tableLimit;
        MAGIC.inline(0x0F, 0x01, 0x5D); MAGIC.inlineOffset(1, tmp); // lidt [ebp-0x08/tmp]
    }

    public static void loadInterruptDescriptorTableRealMode()
    {
        int tableLimit = 1023; // Byte count in table
        long tmp = (long)0 | (long)tableLimit; // 0 is the table base address
        MAGIC.inline(0x0F, 0x01, 0x5D); MAGIC.inlineOffset(1, tmp); // lidt [ebp-0x08/tmp]
    }



    /**
     * Initialize PICs.
     */
    private static void initPic() {
        programChip(MASTER, 0x20, 0x04); //init offset and slave config of master
        programChip(SLAVE, 0x28, 0x02); //init offset and slave config of slave
    }


    /**
     * IRQ initialization.
     */
    private static void programChip(int port, int offset, int icw3) {
        MAGIC.wIOs8(port++, (byte)0x11); // ICW1
        MAGIC.wIOs8(port, (byte)offset); // ICW2
        MAGIC.wIOs8(port, (byte)icw3); // ICW3
        MAGIC.wIOs8(port, (byte)0x01); // ICW4
    }


    private static void buildIDT() {
        int lastIDTEntry = IDT_BASE_ADDRESS;

        // 48 interrupt handlers required - therefore 48 descriptors are required as well
        for(int i = 0; i < IDT_ENTRY_COUNT; i++) {
            buildIDTEntry(lastIDTEntry, i);
            lastIDTEntry += 8;
        }

        // Set Interrupt Descriptor Table Register (IDTR)
        int tableLimit = lastIDTEntry;
        long tmp=(((long)IDT_BASE_ADDRESS) << 16) | (long)tableLimit;
        MAGIC.inline(0x0F, 0x01, 0x5D);
        MAGIC.inlineOffset(1, tmp); // lidt [ebp-0x08/tmp]
    }


    private static void buildIDTEntry(int targetAddress, int entryIndex) {
        int handlerCodeStart = getInterruptHandlerAddress(entryIndex);

        int lowerOffset = handlerCodeStart & 0xFFFF;
        long upperOffset = (long)(handlerCodeStart & 0xFFFF0000) << 32;
        long descriptorEntry = lowerOffset;

        // Set segment descriptor to 1000b Segment | Table Indicator (0: GDT/1: LDT), Priv. Level 0-3
        int segmentSelector = 8 << 16;
        descriptorEntry |= segmentSelector;

        // Set required flags: present bit, privilege level, offset width
        long flags = 0x8E0000000000L;
        descriptorEntry |= flags;
        descriptorEntry |= upperOffset;

        MAGIC.wMem64(targetAddress, descriptorEntry);
    }


    private static int getInterruptHandlerAddress(int entryIndex) {
        // Unfortunately, all following strings need to be constant, making the following code ugly as hell.

        int classRef = MAGIC.cast2Ref(MAGIC.clssDesc("Interrupt"));
        int methodOffset = 0;

        switch (entryIndex) {
            case 0:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleDivisionException");
                break;
            case 1:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleDebugException");
                break;
            case 2:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleNMI");
                break;
            case 3:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleBreakpoint");
                break;
            case 4:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleIntOverflow");
                break;
            case 5:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleIndexOutOfRange");
                break;
            case 6:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleInvalidOpcode");
                break;
            case 7:
                // Reserved
                break;
            case 8:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleDoubleFault");
                break;
            case 9:
            case 10:
            case 11:
            case 12:
                // Reserved
                break;
            case 13:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleGeneralProtectionError");
                break;
            case 14:
                methodOffset = MAGIC.mthdOff("Interrupt", "handlePageFault");
                break;
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
                // Reserved
                break;
            case 32:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleTimer");
                break;
            case 33:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleKeyboard");
                break;
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
                methodOffset = MAGIC.mthdOff("Interrupt", "handleOtherDevices");
                break;
        }

        return MAGIC.rMem32(classRef + methodOffset) + MAGIC.getCodeOff();
    }

    // HANDLERS //

    @SJC.Interrupt
    public static void handleDivisionException() {
        Console.println("Division exception.");
    }

    @SJC.Interrupt
    public static void handleDebugException() {
        Console.println("Debug exception.");
    }

    @SJC.Interrupt
    public static void handleNMI() {
        Console.println("NMI interrupt.");
    }

    @SJC.Interrupt
    public static void handleBreakpoint() {
        Console.println("Breakpoint reached.");
    }

    @SJC.Interrupt
    public static void handleIntOverflow() {
        Console.println("Integer overflow.");
    }

    @SJC.Interrupt
    public static void handleIndexOutOfRange() {
        Console.println("Index out of range.");
    }

    @SJC.Interrupt
    public static void handleInvalidOpcode() {
        Console.println("Invalid opcode.");
    }

    @SJC.Interrupt
    public static void handleDoubleFault(int param) {
        Console.println("Double fault.");
    }

    @SJC.Interrupt
    public static void handleGeneralProtectionError(int param) {
        Console.println("General protection error.");
    }

    @SJC.Interrupt
    public static void handlePageFault(int param) {
        Console.println("Page fault.");
    }

    @SJC.Interrupt
    public static void handleTimer() {
        // Confirm first to unlock the port
        MAGIC.wIOs8(MASTER, (byte)0x20);
        Timer.updateTimer();
    }

    @SJC.Interrupt
    public static void handleKeyboard() {
        MAGIC.wIOs8(MASTER, (byte)0x20);
        int scancode = (MAGIC.rIOs8(0x60) & 0xFF);
        Keyboard.handleScancode(scancode);

    }
    @SJC.Interrupt
    public static void handleOtherDevices() {
        Console.println("Other device interrupt.");
    }
}
