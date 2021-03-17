package IO;

/*
This class can be static, as Console I/O is basically being used all the time.
 */
public class Console {
    // Set some fix values
    private static final int VIDEO_MEMORY_BASE = 0xB8000;
    private static final int CHARACTER_AMOUNT = 2000;
    private static final int SCREEN_WIDTH = 80;
    private static final int SCREEN_HEIGHT = 25;

    private static int _videoMemoryPosition = 0xB8000;
    private static int _caretX = 0;
    private static int _caretY = 0;
    private static byte _currentColor = ConsoleColor.Gray;


    public static void println(String text) {
        for (int i=0; i<text.length(); i++) {
            print(text.charAt(i));
        }
        print('\n');
    }


    /**
     * Prints a string, beginning at the current caret position.
     * @param text The string to print.
     */
    public static void print(String text) {
        for (int i=0; i<text.length(); i++) {
            print(text.charAt(i));
        }
    }


    public static void print(int number) {
        // TODO
    }


    /**
     * Prints a single character to the current caret position.
     * @param c The character to print.
     */
    public static void print(char c) {
        switch (c) {
            case '\n':
                breakLine();
                return;
            case '\r':
                returnCarriage();
                return;
        }
        MAGIC.wMem8(_videoMemoryPosition++, (byte)c);
        MAGIC.wMem8(_videoMemoryPosition++, _currentColor);
    }

    /**
     * Clears the entire screen and resets the caret to 0,0 position.
     */
    public static void clear()
    {
        resetCaret();
        for (int i = 0; i < CHARACTER_AMOUNT; i++)
        {
            MAGIC.wMem8(_videoMemoryPosition++, (byte)0);
        }
        resetCaret();
    }


    /**
     * Set the caret to a new position on the screen.
     * @param x X Position of the cursor.
     * @param y Y Position of the cursor.
     */
    public static void setCursor(int x, int y)
    {
        if(x >= 0 && x < SCREEN_WIDTH && y >= 0 && y < SCREEN_HEIGHT)
        {
            _caretX = x;
            _caretY = y;
            _videoMemoryPosition = getMemPositionFromCaretPosition();
        }
    }


    /**
     * Sets the current text color. Use it with the ConsoleColor class to get the values.
     * @param foregroundColor The foreground color.
     * @param backgroundColor The background color.
     * @param bright Whether the color shall be of a bright tone.
     * @param blink Whether the cursor shall blink.
     */
    public static void setColor(byte foregroundColor, byte backgroundColor, boolean bright, boolean blink)
    {
        // Set foregroundColors first; bits 0-2
        byte color = foregroundColor;
        // Bitshift the background color bits to the correct position; bits 4-6
        color |= backgroundColor << 4;
        // Apply the bright bit if chosen; bit 3
        if(bright) {
            color |= 0x08;
        }
        // Apply the blink bit if chosen; bit 7
        if(blink) {
            color |= 0x80;
        }
        _currentColor = color;
    }


    /**
     * Resets the caret to 0,0 position.
     */
    private static void resetCaret() {
        // For some reason, getting the value from _videoMemoryBase just won't work.
        _videoMemoryPosition = VIDEO_MEMORY_BASE;
        _caretX = 0;
        _caretY = 0;
    }


    /**
     * Places a line break.
     */
    private static void breakLine() {
        _caretX = 0;
        ++_caretY;
        _videoMemoryPosition = getMemPositionFromCaretPosition();
    }


    /**
     * Returns the caret to the first position of the same line.
     */
    private static void returnCarriage() {
        _caretX = 0;
        _videoMemoryPosition = getMemPositionFromCaretPosition();
    }


    private static int getMemPositionFromCaretPosition()
    {
        return VIDEO_MEMORY_BASE + (_caretY * (SCREEN_WIDTH << 1)) + (_caretX << 1);
    }


    /**
     * Defines color codes for console output
     */
    public static class ConsoleColor {
        // No enums, so a class of consts. Not great and a bit whacky, but it works for now ¯\_(ツ)_/¯
        public static final byte Black = 0x00;
        public static final byte Blue = 0x01;
        public static final byte Green = 0x02;
        public static final byte Turquoise = 0x03;
        public static final byte Red = 0x04;
        public static final byte Purple = 0x05;
        public static final byte Brown = 0x06;
        public static final byte Gray = 0x07;
    }
}
