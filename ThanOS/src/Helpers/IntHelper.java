package Helpers;

import Collections.CharStack;
import IO.Console;

// Does not work yet - requires arrays, which are not yet present.
@SJC.IgnoreUnit
public class IntHelper {
    public String toString(int number) {
        CharStack stack = new CharStack();
        while (number > 0) {
            int digit = number % 10;
            stack.push((char)(digit + 48));
            number /= 10;
        }

        String string = new String();
        int stackSize = stack.getSize();
        for (int i = 0; i < stackSize; i++) {
            char temp = stack.pop();
            Console.print(temp);
        }
        return string;
    }
}
