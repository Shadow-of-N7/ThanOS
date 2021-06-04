package devices;

import collections.IntRingBuffer;

public class Keyboard {
    private static IntRingBuffer _buffer;
    // These are not publicly exposed, as they are of no interest to other parts of the OS.
    private static boolean _isExtension0Active = false;
    private static boolean _isExtension1Active = false;
    private static int _lastKeyCode = 0;
    private static boolean _newKeyAvailable = false;

    public static KeyboardState State;
    public static boolean redirectBreakCodes = false;

    public static void initialize() {
        _buffer = new IntRingBuffer(3);
        State = new KeyboardState();
    }


    /**
     * Called when a new scan code arrives from an interrupt.
     * Handles caps lock, num lock and scroll lock.
     * @param scanCode The scan code to interpret.
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
     * Periodically called. Handles conversion of scancodes to keycodes and status management.
     */
    public static void handleKeyBuffer() {
        if(!_buffer.currentElementEmpty()) {

            int scanCode = _buffer.currentAndClear();
            int keyCode;

            // Handle make codes
            if(scanCode >= 0x1 && scanCode <= 0x5D) {
                keyCode = getKeyCode(scanCode);
                setStatus(keyCode, true);
                _isExtension0Active = false;
                _isExtension1Active = false;
                _lastKeyCode = keyCode;
                _newKeyAvailable = true;
            }
            // Handle break codes
            if(scanCode >= 0x81 && scanCode <= 0xDD) {
                // In order to toggle the correct states, we need to fake the correct make
                // codes by just subtracting 128.
                setStatus(getKeyCode(scanCode - 128), false);
                _isExtension0Active = false;
                _isExtension1Active = false;

                if(redirectBreakCodes) {
                    keyCode = scanCode;
                    _lastKeyCode = keyCode;
                    _newKeyAvailable = true;
                }
            }

            // Handle extension 0 codes
            if(scanCode == 0xE0) {
                _isExtension0Active = true;
            }

            // Handle extension 1 codes
            if(scanCode == 0xE1) {
                _isExtension1Active =! _isExtension1Active;
            }
        }
    }


    public static boolean isNewKeyAvailable() {
        return _newKeyAvailable;
    }


    public static int getKeyCode() {
        if(_newKeyAvailable) {
            _newKeyAvailable = false;
            return _lastKeyCode;
        }
        return 0;
    }


    public static boolean isPrintable(int keyCode) {
        return keyCode > 31 && keyCode < 160;
    }


    /**
     * Returns the last character received from the keyboard (if printable).
     * That means keys like CTRL, ALT etc. cannot be fetched.
     * @return The last character received from the keyboard (if printable)
     */
    public static char getChar(int keyCode) {
        char character = (char)219;
        switch (keyCode)
        {
            case KeyCode.Space: character = ' '; break;
            case KeyCode.A: character = 'A'; break;
            case KeyCode.B: character = 'B'; break;
            case KeyCode.C: character = 'C'; break;
            case KeyCode.D: character = 'D'; break;
            case KeyCode.E: character = 'E'; break;
            case KeyCode.F: character = 'F'; break;
            case KeyCode.G: character = 'G'; break;
            case KeyCode.H: character = 'H'; break;
            case KeyCode.I: character = 'I'; break;
            case KeyCode.J: character = 'J'; break;
            case KeyCode.K: character = 'K'; break;
            case KeyCode.L: character = 'L'; break;
            case KeyCode.M: character = 'M'; break;
            case KeyCode.N: character = 'N'; break;
            case KeyCode.O: character = 'O'; break;
            case KeyCode.P: character = 'P'; break;
            case KeyCode.Q: character = 'Q'; break;
            case KeyCode.R: character = 'R'; break;
            case KeyCode.S: character = 'S'; break;
            case KeyCode.T: character = 'T'; break;
            case KeyCode.U: character = 'U'; break;
            case KeyCode.V: character = 'V'; break;
            case KeyCode.W: character = 'W'; break;
            case KeyCode.X: character = 'X'; break;
            case KeyCode.Y: character = 'Y'; break;
            case KeyCode.Z: character = 'Z'; break;
            case KeyCode.A + 32: character = 'a'; break;
            case KeyCode.B + 32: character = 'b'; break;
            case KeyCode.C + 32: character = 'c'; break;
            case KeyCode.D + 32: character = 'd'; break;
            case KeyCode.E + 32: character = 'e'; break;
            case KeyCode.F + 32: character = 'f'; break;
            case KeyCode.G + 32: character = 'g'; break;
            case KeyCode.H + 32: character = 'h'; break;
            case KeyCode.I + 32: character = 'i'; break;
            case KeyCode.J + 32: character = 'j'; break;
            case KeyCode.K + 32: character = 'k'; break;
            case KeyCode.L + 32: character = 'l'; break;
            case KeyCode.M + 32: character = 'm'; break;
            case KeyCode.N + 32: character = 'n'; break;
            case KeyCode.O + 32: character = 'o'; break;
            case KeyCode.P + 32: character = 'p'; break;
            case KeyCode.Q + 32: character = 'q'; break;
            case KeyCode.R + 32: character = 'r'; break;
            case KeyCode.S + 32: character = 's'; break;
            case KeyCode.T + 32: character = 't'; break;
            case KeyCode.U + 32: character = 'u'; break;
            case KeyCode.V + 32: character = 'v'; break;
            case KeyCode.W + 32: character = 'w'; break;
            case KeyCode.X + 32: character = 'x'; break;
            case KeyCode.Y + 32: character = 'y'; break;
            case KeyCode.Z + 32: character = 'z'; break;
            case KeyCode.d1: case KeyCode.NUM_1: character = '1'; break;
            case KeyCode.d2: case KeyCode.NUM_2: character = '2'; break;
            case KeyCode.d3: case KeyCode.NUM_3: character = '3'; break;
            case KeyCode.d4: case KeyCode.NUM_4: character = '4'; break;
            case KeyCode.d5: case KeyCode.NUM_5: character = '5'; break;
            case KeyCode.d6: case KeyCode.NUM_6: character = '6'; break;
            case KeyCode.d7: case KeyCode.NUM_7: character = '7'; break;
            case KeyCode.d8: case KeyCode.NUM_8: character = '8'; break;
            case KeyCode.d9: case KeyCode.NUM_9: character = '9'; break;
            case KeyCode.d0: case KeyCode.NUM_0: character = '0'; break;
            case KeyCode.Tilde: character = '~'; break; // Symbols
            case KeyCode.GraveAccent: character = '`'; break;
            case KeyCode.ExclamationMark: character = '!'; break;
            case KeyCode.At: character = '@'; break;
            case KeyCode.NumberSign: character = '#'; break;
            case KeyCode.Dollar: character = '$'; break;
            case KeyCode.Percent: character = '%'; break;
            case KeyCode.Degree: character = (char)248; break;
            case KeyCode.Caret: character = '^'; break;
            case KeyCode.And: character = '&'; break;
            case KeyCode.Asterisk: case KeyCode.NUM_Asterisk: character = '*'; break;
            case KeyCode.LeftBracket: character = '('; break;
            case KeyCode.RightBracket: character = ')'; break;
            case KeyCode.Hyphen: case KeyCode.NUM_Minus: character = '-'; break;
            case KeyCode.Underscore: character = '_'; break;
            case KeyCode.Plus: case KeyCode.NUM_Plus: character = '+'; break;
            case KeyCode.Equal: character = '='; break;
            case KeyCode.LeftCurlyBracket: character = '{'; break;
            case KeyCode.RightCurlyBracket: character = '}'; break;
            case KeyCode.LeftSquareBracket: character = '['; break;
            case KeyCode.RightSquareBracket: character = ']'; break;
            case KeyCode.VerticalBar: character = '|'; break;
            case KeyCode.Backslash: character = '\\'; break;
            case KeyCode.Slash: case KeyCode.NUM_Slash: character = '/'; break;
            case KeyCode.Colon: character = ':'; break;
            case KeyCode.Semicolon: character = ';'; break;
            case KeyCode.QuotationMark: character = '"'; break;
            case KeyCode.Apostrophe: character = '\''; break;
            case KeyCode.LessThan: character = '<'; break;
            case KeyCode.GreaterThan: character = '>'; break;
            case KeyCode.Comma: case KeyCode.NUM_Comma: character = ','; break;
            case KeyCode.Period: character = '.'; break;
            case KeyCode.QuestionMark: character = '?'; break;
            case KeyCode.Ae: character = 'A'; break;
            case KeyCode.Oe: character = 'O'; break;
            case KeyCode.Ue: character = 'U'; break;
            case KeyCode.ae: character = 'a'; break;
            case KeyCode.oe: character = 'o'; break;
            case KeyCode.ue: character = 'u'; break;
            case KeyCode.Enter: case KeyCode.NUM_Enter: character = '\n'; break;
        }
        return character;

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


    /**
     * Converts a given scan code into a key code.
     * @param scanCode The scan code received taken from an interrupt.
     * @return The key code.
     */
    private static int getKeyCode(int scanCode) {
        int keyCode = 0;
        boolean isUppercase = State.IsShift ^ State.IsCapsLock;
        boolean isNumLock = State.IsNumLock;
        boolean isAltGr = State.IsAltGr;

        switch (scanCode) {
            case 1:
                keyCode = KeyCode.Escape;
                break;
            case 2:
                keyCode = KeyCode.d1;
                if(isUppercase) keyCode = KeyCode.ExclamationMark;
                break;
            case 3:
                keyCode = KeyCode.d2;
                if(isUppercase) keyCode = KeyCode.QuotationMark;
                break;
            case 4:
                keyCode = KeyCode.d3;
                if(isUppercase) keyCode = KeyCode.Section;
                break;
            case 5:
                keyCode = KeyCode.d4;
                if(isUppercase) keyCode = KeyCode.Dollar;
                break;
            case 6:
                keyCode = KeyCode.d5;
                if(isUppercase) keyCode = KeyCode.Percent;
                break;
            case 7:
                keyCode = KeyCode.d6;
                if(isUppercase) keyCode = KeyCode.And;
                break;
            case 8:
                keyCode = KeyCode.d7;
                if(isUppercase) keyCode = KeyCode.Slash;
                if(isAltGr) keyCode = KeyCode.LeftCurlyBracket;
                break;
            case 9:
                keyCode = KeyCode.d8;
                if(isUppercase) keyCode = KeyCode.LeftBracket;
                if(isAltGr) keyCode = KeyCode.LeftSquareBracket;
                break;
            case 10:
                keyCode = KeyCode.d9;
                if(isUppercase) keyCode = KeyCode.RightBracket;
                if(isAltGr) keyCode = KeyCode.RightSquareBracket;
                break;
            case 11:
                keyCode = KeyCode.d0;
                if(isUppercase) keyCode = KeyCode.Equal;
                if(isAltGr) keyCode = KeyCode.RightCurlyBracket;
                break;
            case 12:
                keyCode = KeyCode.SharpS;
                if(isUppercase) keyCode = KeyCode.QuestionMark;
                if(isAltGr) keyCode = KeyCode.Backslash;
                break;
            case 13:
                keyCode = KeyCode.AcuteAccent;
                if(isUppercase) keyCode = KeyCode.GraveAccent;
                break;
            case 14:
                keyCode = KeyCode.Backspace;
                break;
            case 15:
                keyCode = KeyCode.Tab;
                break;
            case 16:
                keyCode = KeyCode.Q;
                if(isAltGr) keyCode = KeyCode.At;
                break;
            case 17:
                keyCode = KeyCode.W;
                break;
            case 18:
                keyCode = KeyCode.E;
                if(isAltGr) keyCode = KeyCode.Euro;
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
                keyCode = KeyCode.Ue;
                break;
            case 27:
                keyCode = KeyCode.Plus;
                if(isUppercase) keyCode = KeyCode.Asterisk;
                if(isAltGr) keyCode = KeyCode.Tilde;
                break;
            case 28:
                keyCode = KeyCode.Enter;
                if(_isExtension0Active) keyCode = KeyCode.NUM_Enter;
                break;
            case 29:
                if (_isExtension0Active) keyCode = KeyCode.RControl;
                else keyCode = KeyCode.Control;
                break;
            case 30:
                keyCode = KeyCode.A;
                break;
            case 31:
                keyCode = KeyCode.S;
                break;
            case 32:
                keyCode = KeyCode.D;
                break;
            case 33:
                keyCode = KeyCode.F;
                break;
            case 34:
                keyCode = KeyCode.G;
                break;
            case 35:
                keyCode = KeyCode.H;
                break;
            case 36:
                keyCode = KeyCode.J;
                break;
            case 37:
                keyCode = KeyCode.K;
                break;
            case 38:
                keyCode = KeyCode.L;
                break;
            case 39:
                keyCode = KeyCode.Oe;
                break;
            case 40:
                keyCode = KeyCode.Ae;
                break;
            case 41:
                keyCode = KeyCode.Caret;
                if(isUppercase) keyCode = KeyCode.Degree;
                break;
            case 42:
                keyCode = KeyCode.Shift;
                break;
            case 43:
                keyCode = KeyCode.NumberSign;
                if(isUppercase) keyCode = KeyCode.Apostrophe;
                break;
            case 44:
                keyCode = KeyCode.Y;
                break;
            case 45:
                keyCode = KeyCode.X;
                break;
            case 46:
                keyCode = KeyCode.C;
                break;
            case 47:
                keyCode = KeyCode.V;
                break;
            case 48:
                keyCode = KeyCode.B;
                break;
            case 49:
                keyCode = KeyCode.N;
                break;
            case 50:
                keyCode = KeyCode.M;
                break;
            case 51:
                keyCode = KeyCode.Comma;
                if (isUppercase) keyCode = KeyCode.Semicolon;
                break;
            case 52:
                keyCode = KeyCode.Period;
                if (isUppercase) keyCode = KeyCode.Colon;
                break;
            case 53:
                keyCode = KeyCode.Hyphen;
                if (isUppercase) keyCode = KeyCode.Underscore;
                break;
            case 54:
                keyCode = KeyCode.RShift;
                break;
            case 55:
                if (isNumLock) keyCode = -1; // No idea what kind of black magic happens here...
                else keyCode = KeyCode.NUM_Asterisk;
                break;
            case 56:
                keyCode = KeyCode.Alt;
                if (_isExtension0Active) keyCode = KeyCode.AltGr;
                break;
            case 57:
                keyCode = KeyCode.Space;
                break;
            case 58:
                keyCode = KeyCode.CapsLock;
                break;
            case 59:
                keyCode = KeyCode.F1;
                break;
            case 60:
                keyCode = KeyCode.F2;
                break;
            case 61:
                keyCode = KeyCode.F3;
                break;
            case 62:
                keyCode = KeyCode.F4;
                break;
            case 63:
                keyCode = KeyCode.F5;
                break;
            case 64:
                keyCode = KeyCode.F6;
                break;
            case 65:
                keyCode = KeyCode.F7;
                break;
            case 66:
                keyCode = KeyCode.F8;
                break;
            case 67:
                keyCode = KeyCode.F9;
                break;
            case 68:
                keyCode = KeyCode.F10;
                break;
            case 69:
                keyCode = KeyCode.NumLock;
                if (_isExtension1Active) keyCode = KeyCode.Pause;
                break;
            case 70:
                keyCode = KeyCode.Roll;
                break;
            case 71:
                if (_isExtension0Active)
                    keyCode = KeyCode.Pos1;
                else {
                    if (isNumLock) keyCode = -1;
                    else keyCode = KeyCode.NUM_7;
                }
                break;
            case 72:
                if (_isExtension0Active) keyCode = KeyCode.ArrowUp;
                else {
                    if (isNumLock) keyCode = -1;
                    else keyCode = KeyCode.NUM_8;
                }
                break;
            case 73:
                if (_isExtension0Active) keyCode = KeyCode.PageUp;
                else {
                    if (isNumLock) keyCode = -1;
                    else keyCode = KeyCode.NUM_9;
                }
                break;
            case 74:
                if (isNumLock) keyCode = -1;
                else keyCode = KeyCode.NUM_Minus;
                break;
            case 75:
                if (_isExtension0Active) keyCode = KeyCode.ArrowLeft;
                else {
                    if (isNumLock) keyCode = -1;
                    else keyCode = KeyCode.NUM_4;
                }
                break;
            case 76:
                if (isNumLock) keyCode = -1;
                else keyCode = KeyCode.NUM_5;
                break;
            case 77:
                if (_isExtension0Active) keyCode = KeyCode.ArrowRight;
                else {
                    if (isNumLock) keyCode = -1;
                    else keyCode = KeyCode.NUM_6;
                }
                break;
            case 78:
                if (isNumLock) keyCode = -1;
                else keyCode = KeyCode.NUM_Plus;
                break;
            case 79:
                if (_isExtension0Active) keyCode = KeyCode.End;
                else {
                    if (isNumLock) keyCode = -1;
                    else keyCode = KeyCode.NUM_1;
                }
                break;
            case 80:
                if (_isExtension0Active) keyCode = KeyCode.ArrowDown;
                else {
                    if (isNumLock) keyCode = -1;
                    else keyCode = KeyCode.NUM_2;
                }
                break;
            case 81:
                if (_isExtension0Active) keyCode = KeyCode.PageDown;
                else {
                    if (isNumLock) keyCode = -1;
                    else keyCode = KeyCode.NUM_3;
                }
                break;
            case 82:
                if (_isExtension0Active) keyCode = KeyCode.Insert;
                else {
                    if (isNumLock) keyCode = -1;
                    else keyCode = KeyCode.NUM_0;
                }
                break;
            case 83:
                if (_isExtension0Active) keyCode = KeyCode.Delete;
                else {
                    if (isNumLock) keyCode = -1;
                    else keyCode = KeyCode.NUM_Comma;
                }
                break;
            case 86:
                keyCode = KeyCode.LessThan;
                if (isUppercase) keyCode = KeyCode.GreaterThan;
                if (isAltGr) keyCode = KeyCode.VerticalBar;
                break;
            case 87:
                keyCode = KeyCode.F11;
                break;
            case 88:
                keyCode = KeyCode.F12;
                break;

        }

        // Apply shift/caps lock to regular letters
        // If both are inactive OR active -> small letter
        // Else: Capital letter.
        if(!isUppercase) {
            // General letters
            if(keyCode >= 65 && keyCode <= 90) {
                keyCode += 32;
            }
            // German umlauts
            switch (keyCode) {
                case KeyCode.Ae:
                    keyCode = KeyCode.ae;
                    break;
                case KeyCode.Oe:
                    keyCode = KeyCode.oe;
                    break;
                case KeyCode.Ue:
                    keyCode = KeyCode.ue;
                    break;
            }
        }
        return keyCode;
    }


    /**
     * Exposes publicly relevant keyboard states.
     */
    public static class KeyboardState {
        public boolean IsCapsLock = false;
        public boolean IsNumLock = true;
        public boolean IsScrollLock = false;
        public boolean IsShift = false;
        public boolean IsCtrl = false;
        public boolean IsAlt = false;
        public boolean IsAltGr = false;
    }
}
