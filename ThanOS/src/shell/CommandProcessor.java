package shell;

import devices.PCI;
import io.Console;
import kernel.Kernel;
import kernel.memory.MemoryMap;

public class CommandProcessor {
    private static final String testGraphics = "testgm";
    private static final String helloWorld = "Hello";
    private static final String cls = "cls";
    private static final String clear = "clear";
    private static final String memMap = "memmap";
    private static final String pciscan = "pciscan";

    public String processCommand(String input) {
        boolean recognized = false;
        if(input.equals(cls) || input.equals(clear)) {
            Console.clear();
            recognized = true;
        }
        if(input.equals(testGraphics))
        {
            Kernel.testGraphicsMode();
            recognized = true;
        }
        if(input.equals(helloWorld)) {
            return "World!";
        }
        if(input.equals(memMap))
        {
            MemoryMap.printMemoryMap2();
            recognized = true;
        }
        if(input.equals(pciscan))
        {
            PCI.printScan();
            recognized = true;
        }
        if(input.equals("help")) {
            String helpString = "memmap: print a memory map.\ntestgm: Executes a short graphics mode test.\nclear/cls: Clears the screen.\npciscan: Scans the PCI bus for devices.";
            return helpString;
        }
        if(input.equals("shutdown")) {
            Kernel.shutdownQEmu();
            return "";
        }

        if(recognized){
            return "";
        }
        else
        {
            return "I am inevitable!";
        }
    }
}
