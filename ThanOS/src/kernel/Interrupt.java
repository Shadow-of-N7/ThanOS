package kernel;

import io.Console;

public class Interrupt {
    private final static int MASTER = 0x20, SLAVE = 0xA0;

    // Put the IDT directly after the GDT and some parts of the bootloader
    private final static int IDT_BASE_ADDRESS = 0x7E00;


    /**
     * Initialize PICs.
     */
    public static void initPic() {
        programChip(MASTER, 0x20, 0x04); //init offset and slave config of master
        programChip(SLAVE, 0x28, 0x02); //init offset and slave config of slave
    }


    private static void programChip(int port, int offset, int icw3) {
        MAGIC.wIOs8(port++, (byte)0x11); // ICW1
        MAGIC.wIOs8(port, (byte)offset); // ICW2
        MAGIC.wIOs8(port, (byte)icw3); // ICW3
        MAGIC.wIOs8(port, (byte)0x01); // ICW4
    }


    /**
     * Start or stop using interrupts.
     * @param start Set to true to start using interrupts; else false.
     */
    public static void useInterrupts(boolean start) {
        if(start) {
            MAGIC.inline(0xFB);
        }
        else {
            MAGIC.inline(0xFA);
        }
    }


    public static void buildIDT() {
        int lastIDTEntry = IDT_BASE_ADDRESS;
        // Counts used space in bytes
        int memCounter = 0;

        // 48 interrupt handlers required - therefore 48 descriptors are required as well
        for(int i = 0; i < 48; i++) {
            buildIDTEntry(lastIDTEntry);
            lastIDTEntry += 8;
            memCounter += 8;
        }

        int tableLimit = lastIDTEntry;

        // Set Interrupt Descriptor Table Register (IDTR)
        long tmp=(((long)IDT_BASE_ADDRESS) << 16) | (long)tableLimit;
        MAGIC.inline(0x0F, 0x01, 0x5D);
        MAGIC.inlineOffset(1, tmp); // lidt [ebp-0x08/tmp]
    }


    private static void buildIDTEntry(int targetAddress) {
        int handlerCodeStart = MAGIC.rMem32(MAGIC.cast2Ref(MAGIC.clssDesc("Interrupt"))
                + MAGIC.mthdOff("Interrupt", "noHandle"))
                + MAGIC.getCodeOff();

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


    // HANDLERS //

    @SJC.Interrupt
    public static void noHandle() {
        Console.println("Handled interrupt!");
        while (true);
    }


    @SJC.Interrupt
    public static void noHandleParam(int code) {
        Console.print("Handled interrupt with code ");
        Console.print(code);
        Console.println("!");
        while (true);
    }
}
