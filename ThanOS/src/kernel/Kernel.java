package kernel;

import collections.CharStack;
import io.Console;
import io.Console.ConsoleColor;
import rte.DynamicRuntime;

public class Kernel {


    public static void main() {

        // Initialization
        Initialize();

        Console console = new Console();

        // Greeting
        console.clear();
        console.setColor(ConsoleColor.Purple, ConsoleColor.Black, false, false);
        console.println("Welcome to ThanOS - The only OS going down south 50% of the time!");
        console.print("Available memory: ");

        // Testing
        console.setColor(ConsoleColor.Green, ConsoleColor.Red, false, false);
        console.setCursor(3, 4);
        console.println("This ist just a test of the console capabilities.\nYou won't be able to do anything, but have a try.");

        console.setColor(ConsoleColor.Gray, ConsoleColor.Black, false, false);
        int testInt = 12347;
        console.print(testInt);
        console.println();

        // Test several new calls - Works
        CharStack foo = new CharStack();
        CharStack bar = new CharStack();
        foo.push('a');
        bar.push('b');
        foo.push('c');
        console.print(foo.pop());
        console.print(bar.pop());
        console.print(foo.pop());
        console.println();
        console.printHex(1489259);
        console.println();
        double pi = 3.14159265D;
        double baz = 0.1;
        console.print(pi, 8);

        console.setCursor(0, 10);
        while(true);
    }


    /**
     * Initializes all required system parameters.
     */
    private static void Initialize() {
        MAGIC.doStaticInit();
        DynamicRuntime.initializeFreeAddresses();
    }
}
