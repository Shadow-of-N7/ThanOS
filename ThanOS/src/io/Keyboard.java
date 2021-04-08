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
            int keyCode;

            // Handle make codes
            if(scanCode >= 0x1 && scanCode <= 0x5D) {
                keyCode = getKeyCode(scanCode);
                setStatus(keyCode, true);
                _isExtension0Active = false;
            }
            // Handle break codes
            if(scanCode >= 0x81 && scanCode <= 0xDD) {
                keyCode = getKeyCode(scanCode);
                setStatus(keyCode, false);
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
     * Sets things like shift, ctrl ...
     * No caps lock, num lock or scroll lock.
     * @param keyCode The key code to which a status shall be set if present.
     * @param status Whether to set to true or false.
     */
    private static void setStatus(int keyCode, boolean status) {
        switch (keyCode) {
            case KeyCode.Shift:
            case KeyCode.RShift:
                State.IsShift = status;
                break;
            case KeyCode.Control:
            case KeyCode.RControl:
                State.IsCtrl = status;
                break;
            case KeyCode.Alt:
                State.IsAlt = status;
                break;
            case KeyCode.AltGr:
                State.IsAltGr = status;
        }
    }


    private static int getKeyCode(int scanCode) {
        int keyCode = 0;
        switch (scanCode) {
            case 1:
                keyCode = KeyCode.Escape;
                break;
            case 2:
                keyCode = KeyCode.d1;
                break;
            case 3:
                keyCode = KeyCode.d2;
                break;
            case 4:
                keyCode = KeyCode.d3;
                break;
            case 5:
                keyCode = KeyCode.d4;
                break;
            case 6:
                keyCode = KeyCode.d5;
                break;
            case 7:
                keyCode = KeyCode.d6;
                break;
            case 8:
                keyCode = KeyCode.d7;
                break;
            case 9:
                keyCode = KeyCode.d8;
                break;
            case 10:
                keyCode = KeyCode.d9;
                break;
            case 11:
                keyCode = KeyCode.d0;
                break;
            case 12:
                keyCode = KeyCode.SharpS;
                break;
            case 13:
                keyCode = KeyCode.AcuteAccent;
                break;
            case 14:
                keyCode = KeyCode.Backspace;
                break;
            case 15:
                keyCode = KeyCode.Tab;
                break;
            case 16:
                keyCode = KeyCode.Q;
                break;
            case 17:
                keyCode = KeyCode.W;
                break;
            case 18:
                keyCode = KeyCode.E;
                break;
            case 19:
                keyCode = KeyCode.R;
                break;
            case 20:
                keyCode = KeyCode.T;
                break;
            case 21:
                keyCode = KeyCode.Z;
                break;
            case 22:
                keyCode = KeyCode.U;
                break;
            case 23:
                keyCode = KeyCode.I;
                break;
            case 24:
                keyCode = KeyCode.O;
                break;
            case 25:
                keyCode = KeyCode.P;
                break;
            case 26:
                keyCode = KeyCode.ue;
                break;
            case 27:
                keyCode = KeyCode.Plus;
                break;
        }

        // Apply shift/caps lock to regular letters
        // If both are inactive OR active -> small letter
        // Else: Capital letter.
        if(State.IsShift == State.IsCapsLock) {
            if(keyCode >= 64 && keyCode <= 90) {
                keyCode += 33;
            }
        }
        else
        {
            // These need to be converted to capital letters
            switch (keyCode) {
                case KeyCode.ae:
                    keyCode = KeyCode.Ae;
                    break;
                case KeyCode.oe:
                    keyCode = KeyCode.Oe;
                    break;
                case KeyCode.ue:
                    keyCode = KeyCode.Ue;
                    break;
            }
        }
        return keyCode;
    }


    /**
     * Exposes publicly relevant keyboard states.
     */
    private static class KeyboardState {
        public boolean IsCapsLock = false;
        public boolean IsNumLock = true;
        public boolean IsScrollLock = false;
        public boolean IsShift = false;
        public boolean IsCtrl = false;
        public boolean IsAlt = false;
        public boolean IsAltGr = false;
    }
}
