package util;

import collections.CharList;
import collections.CharStack;

public class StringConverter {
    private static final char[] hexChars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8' ,'9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String toString(byte num) {
        return toString((long)num);
    }

    public static String toString(int num) {
        return toString((long)num);
    }

    public static String toString(long number) {
        boolean isNegative = false;
        if(number < 0) {
            isNegative = true;
        }
        if(number == 0) {
            return "0";
        }
        CharStack stack = new CharStack();
        while (number > 0) {
            int digit = (int)(number % 10);
            stack.push((char)(digit + 48));
            number /= 10;
        }

        int stackSize = stack.getSize();
        CharList chars = new CharList();
        if(isNegative) {
            chars.add('-');
        }
        for (int i = 0; i < stackSize; i++) {
            char temp = stack.pop();
            chars.add(temp);
        }
        return new String(chars.toArray());
    }

    public static String toString(double number, int precision) {
        // First print all pre-separator digits
        CharStack stack = new CharStack();
        while (number > 0) {
            int digit = ((int)number % 10);
            stack.push((char)(digit + 48));
            number /= 10;
            if(number < 1)
            {
                break;
            }
        }

        int stackSize = stack.getSize();
        CharList chars = new CharList();
        for (int i = 0; i < stackSize; i++) {
            char temp = stack.pop();
            chars.add(temp);
        }
        // Separator
        chars.add('.');

        // Remove the pre-separator digit
        number -= (int)number;

        CharList list = new CharList();
        while (number > 0) {
            // Multiply the after-separator value by 10 to get the digit
            int digit = (int)(number * 10);
            list.add((char)(digit + 48));
            // Num * 10 - digit -> 0.14 * 10 = 1.4, digit 1 -> 1.4 - digit = 0.4
            number = (number * 10) - digit;
        }

        // Take everything from the list and omit everything beyond the precision limit
        int digitCount = list.getLength();
        int digitCounter = 0;
        for (int i = 1; i <= digitCount; i++) {
            if(digitCounter++ >= precision) {
                return new String(chars.toArray());
            }
            char temp = list.elementAt(i);
            chars.add(temp);
        }
        return new String(chars.toArray());
    }

    public static String toString(float number, int precision) {
        return toString((double)number, precision);
    }

    public static String toHexString(long number) {
        CharStack chars = new CharStack();
        CharList charList = new CharList();
        if (number == 0) {
            charList.add('0');
            return new String(charList.toArray());
        }

        while (number > 0) {
            chars.push(hexChars[(int) number % 16]);
            number /= 16;
        }

        int charSize = chars.getSize();
        for(int j = 0; j < charSize; j++) {
            charList.add(chars.pop());
        }
        return new String(charList.toArray());
    }
}
