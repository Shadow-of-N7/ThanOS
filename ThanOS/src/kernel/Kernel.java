package kernel;

import io.Console;
import io.Console.ConsoleColor;
import rte.DynamicRuntime;
import rte.Interrupt;

public class Kernel {


    public static void main() {

        // Initialization
        Initialize();
        // Greeting
        Console.clear();
        Console.setColor(ConsoleColor.Purple, ConsoleColor.Black, false, false);
        Console.println("Welcome to ThanOS - The only OS going down south 50% of the time!");

        // Testing
        Console.setColor(ConsoleColor.Green, ConsoleColor.Red, false, false);
        Console.setCursor(3, 4);
        Console.println("This ist just a test of the console capabilities.\nYou won't be able to do anything, but have a try.");

        while(true);
    }


    /**
     * Initializes all required system parameters.
     */
    private static void Initialize() {
        MAGIC.doStaticInit();
        DynamicRuntime.initializeFreeAddresses();
        Interrupt.useInterrupts(false);
    }
}
