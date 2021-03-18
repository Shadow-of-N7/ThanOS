package kernel;

import Collections.CharStack;
import IO.Console;
import IO.Console.ConsoleColor;
import rte.DynamicRuntime;

public class Kernel {


    public static void main() {

        // Initialization
        MAGIC.doStaticInit();
        DynamicRuntime.initializeFreeAddresses();

        Console console = new Console();

        // Greeting
        console.clear();
        console.setColor(ConsoleColor.Purple, ConsoleColor.Black, false, false);
        console.print("Welcome to ThanOS - The only OS going down south 50% of the time!");

        // Testing
        console.setColor(ConsoleColor.Green, ConsoleColor.Red, false, false);
        console.setCursor(3, 2);
        console.println("This ist just a test of the console capabilities.\nYou won't be able to do anything, but have a try.");

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

        while(true);
    }
}
