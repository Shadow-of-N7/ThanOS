package shell;

import io.Console;
import kernel.Kernel;

public class CommandProcessor {
    private static final String testGraphics = "testgm";
    private static final String helloWorld = "Hello";
    private static final String cls = "cls";
    private static final String clear = "clear";

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
        if(input.equals("shutdown"))
        {
            MAGIC.inline(0x15);
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
