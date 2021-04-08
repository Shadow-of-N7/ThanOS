package kernel;

import io.Console;
import io.Console.ConsoleColor;
import io.KeyCode;
import io.Keyboard;
import rte.BIOS;
import rte.DynamicRuntime;

public class Kernel {
    public static void main() {

        // Initialization
        Initialize();
        // Greeting
        Console.clear();

        // Deactivate caret
        //MAGIC.wIOs8(0x3D4, (byte)0x0A);
        //MAGIC.wIOs8(0x3D5, (byte)0x20);

        //testGraphicsMode();

        Console.setColor(ConsoleColor.Purple, ConsoleColor.Black, false, false);

        Console.println("Welcome to ThanOS - The only OS going down south 50% of the time!");
        Console.setColor(ConsoleColor.Gray, ConsoleColor.Black, false, false);

        while(true) {
            // Updates keyboard buffers; keyboards won't work without this.
            Keyboard.handleKeyBuffer();

            handleInput();
        }
    }


    public static void handleInput() {
        if(Keyboard.isNewKeyAvailable()) {
            int keyCode = Keyboard.getKeyCode();

            // Print char if printable
            if(Keyboard.isPrintable(keyCode)) {
                Console.print(Keyboard.getChar(keyCode));
            }

            // Function keys
            switch (keyCode) {
                case KeyCode.Backspace:
                    if(Console.getCaretX() > 0) {
                        Console.setCaret(Console.getCaretX() - 1, Console.getCaretY());
                        Console.deleteChar();
                    }
                    break;
                case KeyCode.ArrowLeft:
                    if(Console.getCaretX() > 0) {
                        Console.setCaret(Console.getCaretX() - 1, Console.getCaretY());
                    }
                    break;
                case KeyCode.ArrowRight:
                    if(Console.getCaretX() < Console.SCREEN_WIDTH) {
                        Console.setCaret(Console.getCaretX() + 1, Console.getCaretY());
                    }
                    break;
            }
        }
    }


    /**
     * Draws a funny pattern for a few seconds, then returns to text mode.
     */
    public static void testGraphicsMode() {
        assert BIOS.regs != null;
        BIOS.regs.EAX=0x0013;
        BIOS.rint(0x10);
        int screenPixels = 320 * 200;
        byte color = 0;

        int currentAddress = 0xA0000;

        for(int i = 0; i < screenPixels; i++) {
            MAGIC.wMem8(currentAddress++, color++);
        }

        Timer.waitReal(64);
        Console.clear();
        BIOS.regs.EAX=0x0003;
        BIOS.rint(0x10);
        Console.clear();
        Console.setCaret(0, 0);
    }


    /**
     * Initializes all required system parameters.
     */
    private static void Initialize() {
        DynamicRuntime.initializeFreeAddresses();
        MAGIC.doStaticInit();
        Interrupt.initialize();
        Interrupt.useInterrupts(true);
        Keyboard.initialize();
    }
}
