package rte;

public class Interrupt {
    private final static int MASTER = 0x20, SLAVE = 0xA0;


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

    }


    public void buildIDTEntry() {

        // Init with 0
        int entry = 0;

        // Set segment descriptor to 1000b Segment | Table Indicator (0: GDT/1: LDT), Priv. Level 0-3
        entry ^= 0x8;


    }
}
