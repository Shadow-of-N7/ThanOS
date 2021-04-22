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
        Console.print("| EAX: ");
        Console.printHex(EAX);
        Console.setCaret(centerX, baseline + 1);
        Console.print("| EBX: ");
        Console.printHex(EBX);
        Console.setCaret(centerX, baseline + 2);
        Console.print("| ECX: ");
        Console.printHex(ECX);
        Console.setCaret(centerX, baseline + 3);
        Console.print("| EDX: ");
        Console.printHex(EDX);
        Console.setCaret(centerX, baseline + 4);
        Console.print("| ESI: ");
        Console.printHex(ESI);
        Console.setCaret(centerX, baseline + 5);
        Console.print("| EDI: ");
        Console.printHex(EDI);
        Console.setCaret(centerX, baseline + 6);
        Console.print("| EBP: ");
        Console.printHex(EBP);
        Console.setCaret(centerX, baseline + 7);
        Console.print("| ESP: ");
        Console.printHex(ESP);
        Console.setCaret(centerX, baseline + 8);
        Console.print("| DS: ");
        Console.printHex(DS);
        Console.setCaret(centerX, baseline + 9);
        Console.print("| ES: ");
        Console.printHex(ES);
        Console.setCaret(centerX, baseline + 10);
        Console.print("| FS: ");
        Console.printHex(FS);
        Console.setCaret(centerX, baseline + 11);
        Console.print("| FLAGS: ");
        Console.printHex(FLAGS);

        // Print some lines for more visual beauty
        int verticalLine = baseline;
        Console.setCaret(centerX, baseline);
        while (Console.getCaretY() < Console.SCREEN_HEIGHT - 1) {
            Console.setCaret(centerX, verticalLine++);
            Console.print('|');
        }
        int horizontalLine = centerX;
        Console.setCaret(horizontalLine, baseline - 1);
        while (Console.getCaretX() < Console.SCREEN_WIDTH - 1) {
            Console.setCaret(horizontalLine++, baseline - 1);
            Console.print('-');
        }
        Console.print('-');
        Console.DisableCursor();

        while (true) {}
    }

    private static void printCallstackEntry(int EIP)
    {
        Console.print("| EIP: ");
        Console.printHex(EIP);
        Console.println();
    }

}
