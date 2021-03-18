package IO;

import Collections.CharList;
import Collections.CharStack;

public class Console {
    // Set some fix values
    private static final int VIDEO_MEMORY_BASE = 0xB8000;
    private static final int SCREEN_WIDTH = 80;
    private static final int SCREEN_HEIGHT = 25;
    private static final int CHARACTER_AMOUNT = SCREEN_HEIGHT * SCREEN_WIDTH;
    private static final int VIDEO_MEMORY_END = VIDEO_MEMORY_BASE + (CHARACTER_AMOUNT << 1);

    private static int _videoMemoryPosition = 0xB8000;
    private static int _caretX = 0;
    private static int _caretY = 0;
    private static byte _currentColor = ConsoleColor.Gray;
    private static final char[] hexChars = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8' ,'9', 'A', 'B', 'C', 'D', 'E', 'F'
    };


    public void println(String text) {
        for (int i=0; i<text.length(); i++) {
            print(text.charAt(i));
        }
        print('\n');
    }

    public void println() {
        print('\n');
    }


    /**
     * Prints a string, beginning at the current caret position.
     * @param text The string to print.
     */
    public void print(String text) {
        for (int i = 0; i < text.length(); i++) {
            print(text.charAt(i));
        }
    }


    /**
     * Prints a char array, beginning at the current caret position.
     * @param text The char array to print.
     */
    public void print(char[] text) {
        for (char c : text) {
            print(c);
        }
    }


    public void print(int number) {
        print((long)number);
    }


    public void print(long number) {
        boolean isNegative = false;
        if(number < 0) {
            isNegative = true;
        }
        CharStack stack = new CharStack();
        while (number > 0) {
            int digit = (int)(number % 10);
            stack.push((char)(digit + 48));
            number /= 10;
        }

        int stackSize = stack.getSize();
        if(isNegative) {
            print('-');
        }
        for (int i = 0; i < stackSize; i++) {
            char temp = stack.pop();
            print(temp);
        }
    }


    /*
    @SJC.IgnoreUnit
    public static void print(double number) {

        // First print all pre-separator digits
        CharStack stack = new CharStack();
        while (number > 0) {
            int digit = (int)(number % 10);
            stack.push((char)(digit + 48));
            number /= 10;
            if(number < 1)
            {
                break;
            }
        }

        int stackSize = stack.getSize();
        for (int i = 0; i < stackSize; i++) {
            char temp = stack.pop();
            Console.print(temp);
        }
        // Separator
        Console.print('.');

        while (number > 0) {
            // Multiply the after-separator value by 10 to get the digit
            int digit = (int)number * 10;
            stack.push((char)(digit + 48));
            // Num * 10 - digit -> 0.14 * 10 = 1.4, digit 1 -> 1.4 - digit = 0.4
            number = (number * 10) - digit;
        }

        stackSize = stack.getSize();
        for (int i = 0; i < stackSize; i++) {
            char temp = stack.pop();
            Console.print(temp);
        }
    }
    */


    /**
     * Prints a single character to the current caret position.
     * @param c The character to print.
     */
    public void print(char c) {
        switch (c) {
            case '\n':
                breakLine();
                return;
            case '\r':
                returnCarriage();
                return;
        }
        if(_videoMemoryPosition >= VIDEO_MEMORY_END) {
            _videoMemoryPosition = VIDEO_MEMORY_BASE;
        }
        MAGIC.wMem8(_videoMemoryPosition++, (byte)c);
        MAGIC.wMem8(_videoMemoryPosition++, _currentColor);
    }


    /**
     * Prints a single character to the current caret position.
     * @param b The character to print.
     */
    public void print(byte b) {
        switch (b) {
            case '\n':
                breakLine();
                return;
            case '\r':
                returnCarriage();
                return;
        }
        MAGIC.wMem8(_videoMemoryPosition++, b);
        MAGIC.wMem8(_videoMemoryPosition++, _currentColor);
    }


    /**
     * Prints a given number in hexadecimal notation.
     * @param number The number to print.
     */
    public void printHex(short number) {
        printHex((long)number);
    }


    /**
     * Prints a given number in hexadecimal notation.
     * @param number The number to print.
     */
    public void printHex(byte number) {
        printHex((long)number);
    }


    /**
     * Prints a given number in hexadecimal notation.
     * @param number The number to print.
     */
    public void printHex(int number) {
        printHex((long)number);
    }


    /**
     * Prints a given number in hexadecimal notation.
     * @param number The number to print.
     */
    public void printHex(long number)
    {
        CharStack chars = new CharStack();

        if (number == 0) {
            print('0');
            return;
        }

        int i = 0;
        while (number > 0) {
            chars.push(hexChars[(int)number % 16]);
            number /= 16;
            i++;
        }

        int charSize = chars.getSize();
        for(int j = 0; j < charSize; j++) {
            print(chars.pop());
        }
    }



    /**
     * Clears the entire screen and resets the caret to 0,0 position.
     */
    public void clear()
    {
        resetCaret();
        for (int i = 0; i < CHARACTER_AMOUNT << 1; i++)
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
    public void setCursor(int x, int y)
    {
        if(x >= 0 && x < SCREEN_WIDTH && y >= 0 && y < SCREEN_HEIGHT)
        {
            _caretX = x;
            _caretY = y;
            _videoMemoryPosition = getMemoryAddressFromCaretPosition();
        }
    }


    /**
     * Sets the current text color. Use it with the ConsoleColor class to get the values.
     * @param foregroundColor The foreground color.
     * @param backgroundColor The background color.
     * @param bright Whether the foreground color shall be of a bright tone.
     * @param blink Whether the background color shall be of a bright tone.
     */
    public void setColor(byte foregroundColor, byte backgroundColor, boolean bright, boolean blink)
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
    private void resetCaret() {
        _videoMemoryPosition = VIDEO_MEMORY_BASE;
        _caretX = 0;
        _caretY = 0;
    }


    /**
     * Places a line break.
     */
    private void breakLine() {
        _caretX = 0;
        ++_caretY;
        _videoMemoryPosition = getMemoryAddressFromCaretPosition();
    }


    /**
     * Returns the caret to the first position of the same line.
     */
    private void returnCarriage() {
        _caretX = 0;
        _videoMemoryPosition = getMemoryAddressFromCaretPosition();
    }


    /**
     * Get the memory address of the current caret position.
     * @return Memory address.
     */
    private int getMemoryAddressFromCaretPosition()
    {
        // Bit shifting required here as every second memory address marks a color code instead if a position.
        // Not shifting would offset by one byte length, resulting in strange behavior.
        return VIDEO_MEMORY_BASE + (_caretY * (SCREEN_WIDTH << 1)) + (_caretX << 1);
    }


    /**
     * Defines color codes for console output
     */
    public static class ConsoleColor {
        // No enums, so a class of constants. Not great and a bit wacky, but it works for now ¯\_(ツ)_/¯
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
