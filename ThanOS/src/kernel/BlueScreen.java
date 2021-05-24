package kernel;

import collections.StringBuilder;
import devices.StaticV24;
import io.Console;
import io.Console.ConsoleColor;
import rte.SClassDesc;
import rte.SMthdBlock;
import rte.SPackage;

public class BlueScreen {

    private static StringBuilder builder;

    public static void raise(String source) {
        raise(source, null);
    }

    public static void raise(String source, String additionalInformation) {
        builder = new StringBuilder();
        Console.clear(ConsoleColor.Gray, ConsoleColor.Blue, true, false);
        Console.print("Caught ");
        Console.print(source);
        Console.println(" exception.");
        for (int i = 0; i < Console.SCREEN_WIDTH; i++) {
            Console.print((byte) 205);
        }
        Console.println("Call stack:");

        // Gather register data
        int ebp = 0; // First EBP pointing to the stack
        MAGIC.inline(0x89, 0x6D);
        MAGIC.inlineOffset(1, ebp); //mov [ebp+xx],ebp

        // First entry
        int oldEBP = MAGIC.rMem32(ebp);

        int EAX = MAGIC.rMem32(ebp + 4);
        int ECX = MAGIC.rMem32(ebp + 4 * 2);
        int EDX = MAGIC.rMem32(ebp + 4 * 3);
        int EBX = MAGIC.rMem32(ebp + 4 * 4);
        //int OLDESP = MAGIC.rMem32(ebp + 4 * 5);
        int EBP = MAGIC.rMem32(ebp + 4 * 6);
        int ESI = MAGIC.rMem32(ebp + 4 * 7);
        int EDI = MAGIC.rMem32(ebp + 4 * 8);

        // First entry is an interrupt handler, so we have to skip oldEBP and all registers from PUSHA
        // PUSHA contains 8 registers -> EAX ECX EDX EBX OLD-ESP EBP ESI EDI
        // So we skip oldEBP + 8 Registers from PUSHA, each being an integer -> 4 bytes each
        int oldEIP = MAGIC.rMem32(ebp + 4 * 9);
        int lastEntryHeight = 0;
        while (oldEBP >= 0x70000 && oldEBP <= 0x9FFFF) {
            lastEntryHeight = printCallstackEntry(oldEIP, oldEBP);
            int newEBP = MAGIC.rMem32(oldEBP);
            if (newEBP < oldEBP)
                break;
            oldEBP = newEBP;
            oldEIP = MAGIC.rMem32(oldEBP + 4);
        }

        // Print register content
        int baseline = 2;
        int spacerX = Console.SCREEN_WIDTH - 26;

        Console.setCaret(spacerX, baseline);
        Console.print("  Register contents (hex)");

        printRegisterEntry("\tEAX:\t", EAX, spacerX, baseline + 1);
        printRegisterEntry("\tEBX:\t", EBX, spacerX, baseline + 2);
        printRegisterEntry("\tECX:\t", ECX, spacerX, baseline + 3);
        printRegisterEntry("\tEDX:\t", EDX, spacerX, baseline + 4);
        printRegisterEntry("\tESI:\t", ESI, spacerX, baseline + 5);
        printRegisterEntry("\tEDI:\t", EDI, spacerX, baseline + 6);
        printRegisterEntry("\tEBP:\t", EBP, spacerX, baseline + 7);
        //printRegisterEntry("\tESP:\t", ESP, spacerX, baseline + 8);
        //printRegisterEntry("\tDS:\t\t", DS, spacerX, baseline + 9);
        //printRegisterEntry("\tES:\t\t", ES, spacerX, baseline + 10);
        //printRegisterEntry("\tFS:\t\t", FS, spacerX, baseline + 11);
        //printRegisterEntry("\tFLAGS:\t", FLAGS, spacerX, baseline + 12);

        // Print some lines for more visual beauty
        int verticalLine = baseline;
        Console.setCaret(spacerX, baseline);
        while (Console.getCaretY() < Console.SCREEN_HEIGHT - 1) {
            Console.setCaret(spacerX, verticalLine++);
            Console.print((byte) 186);
        }
        int horizontalLine = spacerX;
        Console.setCaret(horizontalLine++, baseline - 1);
        Console.print((byte) 203);
        while (Console.getCaretX() < Console.SCREEN_WIDTH - 1) {
            Console.setCaret(horizontalLine++, baseline - 1);
            Console.print((byte) 205);
        }
        Console.print((byte) 205);

        if (additionalInformation != null) {
            horizontalLine = 0;
            Console.setCaret(0, lastEntryHeight + 1);
            while (Console.getCaretX() < spacerX) {
                Console.setCaret(horizontalLine++, lastEntryHeight + 1);
                Console.print((byte) 205);
            }
            Console.setCaret(horizontalLine++, lastEntryHeight + 1);
            Console.print((byte) 185);

            Console.setPrintableWidth(spacerX);
            Console.println(additionalInformation);
        }
        Console.DisableCursor();

        while (true) {
        }
    }

    /**
     * Prints a call stack entry.
     *
     * @param eip
     * @return Current Cursor Y position.
     */
    private static int printCallstackEntry(int eip, int ebp) {
        Console.print("\tEIP: ");
        Console.printHex(eip);
        //Console.print(", EBP: ");
        //Console.printHex(ebp);
        Console.print(' ');
        Console.print(searchPackage(SPackage.root.subPacks, eip));
        Console.println();
        return Console.getCaretY();
    }

    private static void printRegisterEntry(String regName, int content, int x, int y) {
        Console.setCaret(x, y);
        Console.print(regName);
        Console.printHex(content);
    }


    private static String searchPackage(SPackage pack, int eip) {
        SClassDesc clss;
        SMthdBlock mthd;
        // Search subpacks if present

        while (pack != null) {
            if(pack.subPacks == null) {
            }
            else {
                String result = searchPackage(pack.subPacks, eip);
                // If subpacks found something, immediately return it and don't search on own level
                if(!result.equals("")) {
                    return result;
                }
            }

            clss = pack.units;
            while (clss != null) {
                mthd = clss.mthds;
                while (mthd != null) {
                    int mthdAddress = MAGIC.cast2Ref(mthd);
                    int mthdStart = mthdAddress - mthd._r_relocEntries * MAGIC.ptrSize;
                    int mthdEnd = mthdAddress + mthd._r_scalarSize;
                    if (eip >= mthdStart && eip < mthdEnd) {
                        builder.add(pack.name);
                        builder.add('.');
                        builder.add(clss.name);
                        builder.add('.');
                        builder.add(mthd.namePar);
                        String stackTraceEntry = builder.toString();
                        StaticV24.println(stackTraceEntry);
                        return stackTraceEntry;
                    }
                    mthd = mthd.nextMthd;
                }
                clss = clss.nextUnit;
            }
            pack = pack.nextPack;
        }
        return "";
    }
}
