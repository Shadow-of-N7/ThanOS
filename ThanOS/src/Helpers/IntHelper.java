package Helpers;

import Collections.IntStack;

public class IntHelper {
    public String toString(int number) {
        IntStack stack = new IntStack();
        while (number > 0) {
            int digit = number % 10;
            stack.push(digit);
            number /= 10;
        }

        char[] chars = new char[stack.getLength()];
        for (int i = 0; i < stack.getLength(); i++) {
            chars[i] = (char) (stack.pop() + 48);
        }
        // Doesn't work yet - TODO
        return new String(chars);
    }
}
