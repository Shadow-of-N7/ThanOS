package shell;

import io.Console;
import kernel.Kernel;
import kernel.Memory;

public class CommandProcessor {
    private static final String testGraphics = "testgm";
    private static final String helloWorld = "Hello";
    private static final String cls = "cls";
    private static final String clear = "clear";
    private static final String memMap = "memmap";

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
            Memory.printMemoryMap();
            recognized = true;
        }
        if(input.equals("help")) {
            String helpString = "memmap: print a memory map.\ntestgm: Executes a short graphics mode test.\nclear/cls: Clears the screen.";
            return helpString;
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
