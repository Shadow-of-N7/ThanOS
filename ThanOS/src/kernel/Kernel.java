package kernel;

import Collections.CharStack;
import Helpers.IntHelper;
import IO.Console;
import IO.Console.ConsoleColor;
import rte.DynamicRuntime;

public class Kernel {


    public static void main() {
        MAGIC.doStaticInit();
        DynamicRuntime.initializeFreeAddresses();

        Console.clear();
        Console.setColor(ConsoleColor.Purple, ConsoleColor.Black, false, false);
        Console.print("Welcome to ThanOS - The only OS going down south 50% of the time!");
        Console.setColor(ConsoleColor.Green, ConsoleColor.Red, false, false);
        Console.setCursor(3, 2);
        Console.println("This ist just a test of the console capabilities.\nYou won't be able to do anything, but have a try.");
        CharStack charStack = new CharStack();
        char test = 'a';
        charStack.push(test);
        charStack.push(test);
        charStack.push(test);

        Console.println("");
        Console.print(charStack.pop());
        Console.print(charStack.pop());
        Console.print(charStack.pop());
        Console.print(charStack.pop());

        Console.println("");
        int testInt = 12347;
        IntHelper intHelper = new IntHelper();
        Console.println(intHelper.toString(testInt));

        while(true);
    }
}
