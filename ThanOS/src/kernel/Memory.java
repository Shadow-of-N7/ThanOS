package kernel;

import io.Console;
import rte.BIOS;

public class Memory {
    public static void getMemoryMap() {

        // http://www.uruk.org/orig-grub/mem64mb.html
        // https://wiki.osdev.org/Detecting_Memory_(x86)#Getting_an_E820_Memory_Map

        int bufferBaseAddress = 0x9FFEB; // Highest free stack memory area - 20 bytes
        int counter = 0;

        BIOS.regs.EBX = 0x0; // Continuation value (Start 0)

        do {
            BIOS.regs.EAX = 0x0000E820; // Get system memory map
            BIOS.regs.EDX = 0x534D4150; // Signature SMAP
            BIOS.regs.ECX = 20; // Buffer size

            // TODO: ES:DI as result buffer
            // DI is the lowest 16 bits of EDI
            // Offset
            BIOS.regs.EDI &= 0xFFFF0000; // Delete the lowest 16 bits
            BIOS.regs.EDI |= 11; // Set the lowest 16 bits

            // Segment
            BIOS.regs.ES = 0;
            BIOS.regs.ES |= 0x9FFE;

            BIOS.rint(0x15); // Execute extended BIOS functions

            int adr = (16 * BIOS.regs.ES) + ((BIOS.regs.EDI << 16) >> 16);
            Console.printHex(MAGIC.rMem64(adr));
            Console.print("Buffer size: ");
            Console.println(BIOS.regs.ECX);
            if((BIOS.regs.FLAGS & 1) == 1) {
                Console.println("ERROR!");
            }

            /*
            if (( BIOS.regs.FLAGS & 1) == 0) {
                Console.println("Carry correct");
            }
            if(BIOS.regs.EAX == 0x534D4150) {
                Console.println("EAX correct");
            }
            */
        }
        while (BIOS.regs.EBX != 0);




    }
}
