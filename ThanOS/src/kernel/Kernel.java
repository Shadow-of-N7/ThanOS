package kernel;

import devices.PCI;
import io.Console;
import io.Console.ConsoleColor;
import devices.KeyCode;
import devices.Keyboard;
import rte.BIOS;
import rte.DynamicRuntime;
import shell.Thash;

public class Kernel {

    public static void main() {

        // Initialization
        Initialize();
        // Greeting
        Console.clear();

        //testGraphicsMode();

        Console.setColor(ConsoleColor.Purple, ConsoleColor.Black, false, false);

        Console.println("Welcome to ThanOS - The only OS going down south 50% of the time!");
        Console.setColor(ConsoleColor.Gray, ConsoleColor.Black, false, false);

        Console.print('>');

        while(true) {
            // Updates keyboard buffers; keyboards won't work without this.
            Keyboard.handleKeyBuffer();

            handleInput();
        }
    }


    public static void handleInput() {
        if(Keyboard.isNewKeyAvailable()) {
            int keyCode = Keyboard.getKeyCode();
            Thash.takeKeyCode(keyCode);
        }
    }


    /**
     * Draws a funny pattern for a few seconds, then returns to text mode.
     */
    public static void testGraphicsMode() {
        BIOS.regs.EAX=0x0013;
        BIOS.rint(0x10);
        int screenPixels = 320 * 200;
        byte color = 0;

        int currentAddress = 0xA0000;

        for(int i = 0; i < screenPixels; i++) {
            MAGIC.wMem8(currentAddress++, color++);
        }

        Timer.waitReal(64);
        //Console.clear();
        BIOS.regs.EAX=0x0003;
        BIOS.rint(0x10);
        Console.clear();
        Console.setCaret(0, 0);
    }


    /**
     * Initializes all required system parameters.
     */
    private static void Initialize() {
        DynamicRuntime.initializeFreeAddresses();
        MAGIC.doStaticInit();
        Interrupt.initialize();
        Interrupt.useInterrupts(true);

        Keyboard.initialize();
        Thash.intialize();
    }


    /**
     * Only woks outside QEmu.
     */
    public static void shutdown() {
        int biosMemory = 0x60000;
        int addr = biosMemory + 8;
        MAGIC.wMem8(addr++, (byte) 0x66);
        MAGIC.wMem8(addr++, (byte) 0xb8);
        MAGIC.wMem8(addr++, (byte) 0x00);
        MAGIC.wMem8(addr++, (byte) 0x10);// mov ax,0x1000
        MAGIC.wMem8(addr++, (byte) 0x66);
        MAGIC.wMem8(addr++, (byte) 0x8c);
        MAGIC.wMem8(addr++, (byte) 0xd0); // mov ax,ss
        MAGIC.wMem8(addr++, (byte) 0x66);
        MAGIC.wMem8(addr++, (byte) 0xbc);
        MAGIC.wMem8(addr++, (byte) 0x00);
        MAGIC.wMem8(addr++, (byte) 0xf0); // mov sp,0xf000
        MAGIC.wMem8(addr++, (byte) 0x66);
        MAGIC.wMem8(addr++, (byte) 0xb8);
        MAGIC.wMem8(addr++, (byte) 0x07);
        MAGIC.wMem8(addr++, (byte) 0x53); // mov    ax,0x5307
        MAGIC.wMem8(addr++, (byte) 0x66);
        MAGIC.wMem8(addr++, (byte) 0xbb);
        MAGIC.wMem8(addr++, (byte) 0x01);
        MAGIC.wMem8(addr++, (byte) 0x00); // mov    bx,0x1
        MAGIC.wMem8(addr++, (byte) 0x66);
        MAGIC.wMem8(addr++, (byte) 0xb9);
        MAGIC.wMem8(addr++, (byte) 0x03);
        MAGIC.wMem8(addr++, (byte) 0x00); // mov    cx,0x3
        BIOS.rint(0x15);;
    }


    public static void shutdownQEmu() {
        MAGIC.wIOs32(0x604, 0x2000);
    }
}
