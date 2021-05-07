package kernel;

import io.Console;
import io.Console.ConsoleColor;

public class BlueScreen {

    public static void raise(String source)
    {
        Console.clear(ConsoleColor.Gray, ConsoleColor.Blue, true, false);
        Console.print("Caught ");
        Console.print(source);
        Console.println(" exception.");
        for(int i = 0; i < Console.SCREEN_WIDTH; i++) {
            Console.print((byte)205);
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
        int oldEIP = MAGIC.rMem32(ebp + (9 << 2));
        while(oldEBP >= 0x70000 && oldEBP <= 0x9FFFF)
        {
            printCallstackEntry(oldEIP);
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
        Console.print("  Register contents (hex):");

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
            Console.print((byte)186);
        }
        int horizontalLine = spacerX;
        Console.setCaret(horizontalLine++, baseline - 1);
        Console.print((byte)203);
        while (Console.getCaretX() < Console.SCREEN_WIDTH - 1) {
            Console.setCaret(horizontalLine++, baseline - 1);
            Console.print((byte)205);
        }
        Console.print((byte)205);
        Console.DisableCursor();

        while (true) {}
    }

    private static void printCallstackEntry(int eip)
    {
        Console.print("\tEIP: ");
        Console.printHex(eip);
        Console.println();

    }

    private static void printRegisterEntry(String regName, int content, int x, int y) {
        Console.setCaret(x, y);
        Console.print(regName);
        Console.printHex(content);
    }
}
