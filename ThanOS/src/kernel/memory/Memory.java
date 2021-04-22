package kernel.memory;

import io.Console;
import kernel.BIOS;

public class Memory {

    public static MemoryMap getMemoryMap() {
        Console.println("Displaying memory map:");
        // New base address: 0x7F80

        // http://www.uruk.org/orig-grub/mem64mb.html
        // https://wiki.osdev.org/Detecting_Memory_(x86)#Getting_an_E820_Memory_Map

        //int bufferBaseAddress = 0x7F80; // Highest free stack memory area - 20 bytes
        MemoryMap map = new MemoryMap();

        BIOS.regs.EBX = 0x0; // Continuation value (Start 0)

        do {
            // Get system memory map
            BIOS.regs.EAX = 0x0000E820;
            // Signature SMAP
            BIOS.regs.EDX = 0x534D4150;
            // Buffer size
            BIOS.regs.ECX = 20;
            // DI is the lowest 16 bits of EDI
            // Offset
            BIOS.regs.EDI &= 0xFFFF0000; // Delete the lowest 16 bits
            BIOS.regs.EDI |= 0x0; // Set the lowest 16 bits
            // Segment
            BIOS.regs.ES = 0x7F8;

            BIOS.rint(0x15); // Execute extended BIOS functions

            int baseAddress = (16 * BIOS.regs.ES) + ((BIOS.regs.EDI << 16) >> 16);
            // The start address of the block
            long blockBaseAddress = MAGIC.rMem64(baseAddress);
            // The length of the block
            long blockLength = MAGIC.rMem64(baseAddress + 8);
            // Whether the block is reserved or free
            int blockType = MAGIC.rMem32(baseAddress + 16);
            map.memoryBlocks.add(new MemoryBlock(blockBaseAddress, blockLength, blockType));
        }
        while (BIOS.regs.EBX != 0);
        return map;
    }

    public static void printMemoryMap() {
        MemoryMap map = getMemoryMap();
        for(int i = 0; i < map.memoryBlocks.getLength(); i++) {
            long blockBaseAddress = map.memoryBlocks.elementAt(i).baseAddress;
            long blockLength = map.memoryBlocks.elementAt(i).blockLength;
            int blockType = map.memoryBlocks.elementAt(i).blockType;

            Console.println();
            Console.print("Block ");
            Console.print(i);
            Console.println(":");

            Console.printHex(blockBaseAddress);
            Console.print("-");
            Console.printHex(blockBaseAddress + blockLength);
            switch (blockType) {
                case 1:
                    Console.setColor(Console.ConsoleColor.Green, Console.ConsoleColor.Black, false, false);
                    Console.print(": Free");
                    break;
                case 2:
                    Console.setColor(Console.ConsoleColor.Red, Console.ConsoleColor.Black, false, false);
                    Console.print(": Reserved");
                    break;
                case 3:
                    Console.print(": ACPI reclaimable");
                    break;
                case 4:
                    Console.print(": ACPI NVS Memory");
                    break;
                case 5:
                    Console.setColor(Console.ConsoleColor.Black, Console.ConsoleColor.Red, false, false);
                    Console.print("Bad memory");
                    break;
            }
            Console.setColor(Console.ConsoleColor.Gray, Console.ConsoleColor.Black, false, false);

            Console.println();
        }
    }
}
