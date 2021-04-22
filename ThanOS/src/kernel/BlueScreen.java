package kernel;

import io.Console;
import io.Console.ConsoleColor;

public class BlueScreen {

    public static void raise(String source)
    {
        // Get register content first
        assert BIOS.regs != null; // Just to silence that damn IDE
        int EAX = BIOS.regs.EAX;
        int EBX = BIOS.regs.EBX;
        int ECX = BIOS.regs.ECX;
        int EDX = BIOS.regs.EDX;
        int ESI = BIOS.regs.ESI;
        int EDI = BIOS.regs.EDI;
        int EBP = BIOS.regs.EBP;
        int ESP = BIOS.regs.ESP;
        int DS = BIOS.regs.DS;
        int ES = BIOS.regs.ES;
        int FS = BIOS.regs.FS;
        int FLAGS = BIOS.regs.FLAGS;

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
        // First entry is an interrupt handler, so we have to skip oldEBP and all registers from PUSHA
        // PUSHA contains 8 registers -> EAX ECX EDX EBX OLD-ESP EBP ESI EDI
        // So we skip oldEBP + 8 Registers from PUSHA, each being an integer -> 4 bytes each
        int oldEIP = MAGIC.rMem32(oldEBP + (9 << 2));
        while(oldEBP >= 0x70000 && oldEBP <= 0x9FFFF)
        {
            printCallstackEntry(oldEIP);
            oldEBP = MAGIC.rMem32(oldEBP);
            oldEIP = MAGIC.rMem32(oldEBP + 4);
        }

        // Print register content
        int baseline = 2;
        int centerX = Console.SCREEN_WIDTH >> 1;

        Console.setCaret(centerX, baseline);
        Console.print("  Register contents:");

        printRegisterEntry("\tEAX:\t", EAX, centerX, baseline + 1);
        printRegisterEntry("\tEBX:\t", EBX, centerX, baseline + 2);
        printRegisterEntry("\tECX:\t", ECX, centerX, baseline + 3);
        printRegisterEntry("\tEDX:\t", EDX, centerX, baseline + 4);
        printRegisterEntry("\tESI:\t", ESI, centerX, baseline + 5);
        printRegisterEntry("\tEDI:\t", EDI, centerX, baseline + 6);
        printRegisterEntry("\tEBP:\t", EBP, centerX, baseline + 7);
        printRegisterEntry("\tESP:\t", ESP, centerX, baseline + 8);
        printRegisterEntry("\tDS:\t\t", DS, centerX, baseline + 9);
        printRegisterEntry("\tES:\t\t", ES, centerX, baseline + 10);
        printRegisterEntry("\tFS:\t\t", FS, centerX, baseline + 11);
        printRegisterEntry("\tFLAGS:\t", FLAGS, centerX, baseline + 12);

        // Print some lines for more visual beauty
        int verticalLine = baseline;
        Console.setCaret(centerX, baseline);
        while (Console.getCaretY() < Console.SCREEN_HEIGHT - 1) {
            Console.setCaret(centerX, verticalLine++);
            Console.print((byte)186);
        }
        int horizontalLine = centerX;
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
