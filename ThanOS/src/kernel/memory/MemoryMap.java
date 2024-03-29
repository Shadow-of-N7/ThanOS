package kernel.memory;

import collections.MemoryBlockList;
import io.Console;
import io.Table;
import kernel.BIOS;
import util.StringConverter;

public class MemoryMap {

    public static MemoryBlockList getMemoryMap() {
        // New base address: 0x7F80

        // http://www.uruk.org/orig-grub/mem64mb.html
        // https://wiki.osdev.org/Detecting_Memory_(x86)#Getting_an_E820_Memory_Map
        MemoryBlockList map = new MemoryBlockList();

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
            map.add(new MemoryBlock(blockBaseAddress, blockLength, blockType));
        }
        while (BIOS.regs.EBX != 0);
        return map;
    }

    public static void printMemoryMap() {
        MemoryBlockList map = getMemoryMap();
        Console.println("Displaying memory map:");
        Console.println();
        Console.println("Block\t|Start\t|End\t|Type");
        for(int i = 0; i < map.getLength(); i++) {
            long blockBaseAddress = map.elementAt(i).baseAddress;
            long blockLength = map.elementAt(i).blockLength;
            int blockType = map.elementAt(i).blockType;
            Console.print(i);
            Console.print("\t|");

            Console.printHex(blockBaseAddress);
            Console.print("\t|");
            Console.printHex(blockBaseAddress + blockLength);
            Console.print("\t|");
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

    public static void printMemoryMap2() {
        MemoryBlockList map = getMemoryMap();
        Table table = new Table();
        //table.setDefaultColor(Console.ConsoleColor.createColor(Console.ConsoleColor.Gray,
        //        Console.ConsoleColor.Blue, true, true));
        table.addRow();
        table.getRow(0).addCell("Block");
        table.getRow(0).addCell("Start address");
        table.getRow(0).addCell("End address");
        table.getRow(0).addCell("Type");

        for(int i = 0; i < map.getLength(); i++) {
            long blockBaseAddress = map.elementAt(i).baseAddress;
            long blockLength = map.elementAt(i).blockLength;
            int blockType = map.elementAt(i).blockType;
            table.addRow();
            table.getRow(i + 1).addCell(StringConverter.toString(i));
            table.getRow(i + 1).addCell(StringConverter.toHexString(blockBaseAddress));
            table.getRow(i + 1).addCell(StringConverter.toHexString(blockBaseAddress + blockLength));

            String type = "";
            byte color = Console.ConsoleColor.Gray;

            switch (blockType) {
                case 1:
                    type = "Free";
                    color = Console.ConsoleColor.createColor(Console.ConsoleColor.Green,
                            Console.ConsoleColor.Black,
                            false,
                            false);
                    break;
                case 2:
                    type = "Reserved";
                    color = Console.ConsoleColor.createColor(Console.ConsoleColor.Red,
                            Console.ConsoleColor.Black,
                            false,
                            false);
                    break;
                case 3:
                    type = "ACPI reclaimable";
                    break;
                case 4:
                    type = "ACPI NVS Memory";
                    break;
                case 5:
                    type = "Bad memory";
                    color = Console.ConsoleColor.createColor(Console.ConsoleColor.Gray,
                            Console.ConsoleColor.Red,
                            true,
                            false);
                    break;
            }
            table.getRow(i + 1).addCell(type).setColor(color);
        }
        table.print();
    }
}
