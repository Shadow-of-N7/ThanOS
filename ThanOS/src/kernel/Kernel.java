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

        // Greeting
        Console.clear();
        Console.setColor(ConsoleColor.Purple, ConsoleColor.Black, false, false);
        Console.print("Welcome to ThanOS - The only OS going down south 50% of the time!");

        // Testing
        Console.setColor(ConsoleColor.Green, ConsoleColor.Red, false, false);
        Console.setCursor(3, 2);
        Console.println("This ist just a test of the console capabilities.\nYou won't be able to do anything, but have a try.");

        int testInt = 12347;
        Console.print(testInt);
        Console.println();

        // Test several new calls - Works
        CharStack foo = new CharStack();
        CharStack bar = new CharStack();
        foo.push('a');
        bar.push('b');
        foo.push('c');
        Console.print(foo.pop());
        Console.print(bar.pop());
        Console.print(foo.pop());

        while(true);
    }
}
