package kernel;

import collections.CharStack;
import io.Console;
import io.Console.ConsoleColor;
import rte.DynamicRuntime;

public class Kernel {
    public static void main() {

        // Initialization
        Initialize();
        // Greeting
        Console.clear();
        Console.setColor(ConsoleColor.Purple, ConsoleColor.Black, false, false);

        Console.println("Welcome to ThanOS - The only OS going down south 50% of the time!");

        // Uncomment to fire an interrupt for debug purposes
        //MAGIC.inline(0xCC);

        //BIOS.regs.EAX=0x0013;
        //BIOS.rint(0x10);
        Console.print("foo");
        CharStack foo = new CharStack();
        foo.push('A');
        Console.print(foo.pop());


        while(true) {
        }
    }


    /**
     * Initializes all required system parameters.
     */
    private static void Initialize() {
        MAGIC.doStaticInit();
        DynamicRuntime.initializeFreeAddresses();
        Interrupt.initialize();
        Interrupt.useInterrupts(true);
    }
}
