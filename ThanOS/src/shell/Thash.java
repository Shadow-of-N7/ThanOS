package shell;

import io.Console;

// Stands for "Thanos shell"
public class Thash {
    private static final char[] _commandBuffer = new char[256];
    private static int _bufferIndex = 0;
    private static CommandProcessor _processor;


    public static void intialize() {
        _processor = new CommandProcessor();
    }


    public static void executeCommand() {
        String command = extractCommand();
        clearBuffer();

        if(command.length() > 0) {
            String result = _processor.processCommand(command);
            if(!result.equals("")) {
                Console.println(result);
            }
        }
        Console.print('>');
    }


    public static void takeChar(char c) {
        _commandBuffer[_bufferIndex++] = c;
    }


    public static void removeChar() {
        _commandBuffer[_bufferIndex--] = 0;
    }


    public static void moveLeft() {
        --_bufferIndex;
    }


    public static void moveRight() {
        ++_bufferIndex;
    }


    private static String extractCommand() {
        int charCounter = 0;
        while (_commandBuffer[charCounter] != 0) {
            ++charCounter;
        }

        if(charCounter == 0) {
            return "";
        }

        char[] temp = new char[charCounter];
        for(int i = 0; i < temp.length; i++) {
            temp[i] = _commandBuffer[i];
        }
        return new String(temp);
    }


    private static void clearBuffer() {
        for(int i = 0; i < _commandBuffer.length; i++) {
            _commandBuffer[i] = 0;
        }
        _bufferIndex = 0;
    }
}
