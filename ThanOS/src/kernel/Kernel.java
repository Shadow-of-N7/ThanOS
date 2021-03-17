package kernel;

import Collections.Stack;
import IO.Console;
import IO.Console.ConsoleColor;
import rte.DynamicRuntime;

public class Kernel {


    public static void main() {
        MAGIC.doStaticInit();
        DynamicRuntime.initializeFreeAddresses();

        Console.clear();
        Console.setColor(ConsoleColor.Purple);
        Console.print("Welcome to ThanOS - The only OS going down south 50% of the time!");
        Console.setColor(ConsoleColor.LightGray);
        Console.setCursor(3, 2);
        Console.println("This ist just a test of the console capabilities.\nYou won't be able to do anything, but have a try.");

        while(true);
    }
}
