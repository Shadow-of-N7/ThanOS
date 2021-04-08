package io;

import collections.Ringbuffer;

public class Keyboard {
    private static Ringbuffer _buffer;
    // These are not publicly exposed, as they are of no interest to other parts of the OS.
    private static boolean _isExtension0Active = false;
    private static boolean _isExtension1Active = false;
    private static char _lastChar = 0x0;

    public static KeyboardState State;


    public static void initialize() {
        _buffer = new Ringbuffer(3);
        State = new KeyboardState();
    }


    /**
     * Called when a new scan code arrives from an interrupt.
     * Handles caps lock, num lock and scroll lock.
     * @param scanCode
     */
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


    /**
     * Periodically called.
     */
    public static void handleKeyBuffer() {
        if(!_buffer.currentElementEmpty()) {

            int scanCode = _buffer.currentAndClear();
            Console.printHex(scanCode);
            Console.print(' ');

            // Handle make codes
            if(scanCode >= 0x1 && scanCode <= 0x5D) {

                _isExtension0Active = false;
            }
            // Handle break codes
            if(scanCode >= 0x81 && scanCode <= 0xDD) {

                _isExtension0Active = false;
            }

            // Handle extension 1 codes
            if(scanCode == 0xE0) {
                _isExtension0Active = true;
            }
        }
    }


    /**
     * Returns the last character received from the keyboard (if printable).
     * That means keys like CTRL, ALT etc. cannot be fetched.
     * @return The last character received from the keyboard (if printable)
     */
    public static char getChar() {
        return _lastChar;
    }


    /**
     * Casts bool values to int. 0 -> false; else -> 1.
     * @param value The value to cast.
     * @return The casted value.
     */
    private static int toInt(boolean value) {
        if(!value) {
            return 0;
        }
        else return 1;
    }


    /**
     * Exposes publicly relevant keyboard states.
     */
    private static class KeyboardState {
        public boolean IsCapsLock = false;
        public boolean IsNumLock = true;
        public boolean IsScrollLock = false;
    }
}
