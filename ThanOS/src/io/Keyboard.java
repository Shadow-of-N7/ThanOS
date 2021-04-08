package io;

import collections.Ringbuffer;

public class Keyboard {
    private static Ringbuffer _buffer;
    public static KeyboardState State;


    public static void initialize() {
        _buffer = new Ringbuffer(3);
        State = new KeyboardState();
    }


    public static void handleScancode(int scanCode) {
        byte configValue = 0;
        if(scanCode == 0x3A) {
            State.IsCapsLock =! State.IsCapsLock;
            configValue |= toInt(State.IsCapsLock) << 2;
        }
        if(scanCode == 0x45) {
            State.IsNumLock =! State.IsNumLock;
            configValue |= toInt(State.IsNumLock) << 1;
        }
        if(scanCode == 0x46) {
            State.IsScrollLock =! State.IsScrollLock;
            configValue |= toInt(State.IsScrollLock);
        }
        if(scanCode == 0x3A || scanCode == 0x45 || scanCode == 0x46) {
            MAGIC.wIOs8(0x64, (byte) 0xED);
            MAGIC.wIOs8(0x60, configValue);
            return;
        }

        _buffer.insert(scanCode);
    }


    public static void handleKeyBuffer() {
        if(!_buffer.currentElementEmpty()) {
            // works so far
            Console.printHex(_buffer.currentAndClear());
            Console.print(' ');
        }
    }


    private static int toInt(boolean value) {
        if(!value) {
            return 0;
        }
        else return 1;
    }


    private static class KeyboardState {
        public boolean IsCapsLock = false;
        public boolean IsNumLock = true;
        public boolean IsScrollLock = false;
    }
}
