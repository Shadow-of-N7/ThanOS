package kernel;

import collections.Ringbuffer;
import io.Console;
import io.Console.ConsoleColor;
import io.Keyboard;
import rte.BIOS;
import rte.DynamicRuntime;

public class Kernel {
    public static void main() {

        // Initialization
        Initialize();
        // Greeting
        Console.clear();

        //testGraphicsMode();

        Console.setColor(ConsoleColor.Purple, ConsoleColor.Black, false, false);

        Console.println("Welcome to ThanOS - The only OS going down south 50% of the time!");

        while(true) {
            Keyboard.handleKeyBuffer();
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
        Console.clear();
        BIOS.regs.EAX=0x0003;
        BIOS.rint(0x10);
        Console.clear();
        Console.setCursor(0, 0);
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
    }
}
