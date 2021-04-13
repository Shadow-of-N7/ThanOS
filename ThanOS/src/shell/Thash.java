package shell;

import devices.KeyCode;
import devices.Keyboard;
import io.Console;

// Stands for "Thanos shell"
public class Thash {
    private static final char[] _commandBuffer = new char[256];
    private static int _bufferIndex = 0;
    private static CommandProcessor _processor;
    private static String[] _commandHistory = new String[50];
    private static int _currentRepeatedCommand = -1;
    private static boolean _isRepeatingMode = false;


    public static void intialize() {
        _processor = new CommandProcessor();
    }


    public static void executeCommand() {
        String command = extractCommand();
        clearBuffer();
        if(!command.equals("")) {
            addCommandToHistory(command);
        }

        if(command.length() > 0) {
            String result = _processor.processCommand(command);
            if(!result.equals("")) {
                Console.println(result);
            }
        }
        _isRepeatingMode = false;
        Console.print('>');
    }


    public static void takeKeyCode(int keyCode) {
        if(Keyboard.isPrintable(keyCode)) {
            char c = Keyboard.getChar(keyCode);
            Console.print(c);
            if(keyCode != KeyCode.Enter) {
                takeChar(c);
            }
        }

        // Function keys
        switch (keyCode) {
            case KeyCode.Backspace:
                if(Console.getCaretX() > 0) {
                    Console.setCaret(Console.getCaretX() - 1, Console.getCaretY());
                    Console.deleteChar();
                    removeChar();
                }
                break;
            case KeyCode.ArrowLeft:
                if(Console.getCaretX() > 0) {
                    Console.setCaret(Console.getCaretX() - 1, Console.getCaretY());
                    moveLeft();
                }
                break;
            case KeyCode.ArrowRight:
                if(Console.getCaretX() < Console.SCREEN_WIDTH) {
                    Console.setCaret(Console.getCaretX() + 1, Console.getCaretY());
                    moveRight();
                }
                break;
            case KeyCode.ArrowUp:
                moveUp();
                break;
            case KeyCode.ArrowDown:
                moveDown();
                break;
            case KeyCode.Enter:
            case KeyCode.NUM_Enter:
                executeCommand();
                break;
            case KeyCode.Escape:
                if(_isRepeatingMode) {
                    _isRepeatingMode = false;
                    Console.clearLine();
                    Console.print('>');
                    _currentRepeatedCommand = -1;
                    clearBuffer();
                }
        }
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

    public static void moveUp() {
        if(_commandHistory[_currentRepeatedCommand + 1] != null) {
            _currentRepeatedCommand++;
        }
        else{ return; }

        if(_currentRepeatedCommand > -1) {
            Console.clearLine();
            Console.print('>');
            Console.print(_commandHistory[_currentRepeatedCommand]);
            clearBuffer();
            for(int i = 0; i < _commandHistory[_currentRepeatedCommand].length(); i++) {
                _commandBuffer[i] = _commandHistory[_currentRepeatedCommand].charAt(i);
            }
            _isRepeatingMode = true;
            _bufferIndex = _commandHistory[_currentRepeatedCommand].length();
        }
        /*
        Console.directPrintInt(_currentRepeatedCommand,
                10,
                Console.SCREEN_WIDTH - 20,
                Console.SCREEN_HEIGHT -1,
                Console.ConsoleColor.Red);
         */
    }


    public static void moveDown() {
        if(_currentRepeatedCommand > 0) {
            _currentRepeatedCommand--;
            Console.clearLine();
            Console.print('>');
            Console.print(_commandHistory[_currentRepeatedCommand]);
            clearBuffer();
            for(int i = 0; i < _commandHistory[_currentRepeatedCommand].length(); i++) {
                _commandBuffer[i] = _commandHistory[_currentRepeatedCommand].charAt(i);
            }
            _bufferIndex = _commandHistory[_currentRepeatedCommand].length();
        }
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


    private static void addCommandToHistory(String command) {
        // Shift all entries on element up
        for(int i = _commandHistory.length - 2; i > 0; i--) {
            _commandHistory[i] = _commandHistory[i - 1];
        }
        _commandHistory[0] = command;
    }
}
