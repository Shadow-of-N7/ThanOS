package io;

public class KeyCode {

    // Special characters - Not printable
    public static final int Escape = 0;
    public static final int Command = 1;
    public static final int Backspace = 2;
    public static final int Enter = 3;
    public static final int Tab = 4;
    public static final int Shift = 5;
    public static final int RShift = 6;
    public static final int Control = 7;
    public static final int RControl = 8;
    public static final int Alt = 9;
    public static final int AltGr = 10;
    public static final int ArrowLeft = 11;
    public static final int ArrowRight = 12;
    public static final int ArrowUp = 13;
    public static final int ArrowDown = 14;
    public static final int Delete = 15;
    public static final int Insert = 16;
    public static final int Pos1 = 17;
    public static final int End = 18;
    public static final int F1 = 19;
    public static final int F2 = 20;
    public static final int F3 = 21;
    public static final int F4 = 22;
    public static final int F5 = 23;
    public static final int F6 = 24;
    public static final int F7 = 25;
    public static final int F8 = 26;
    public static final int F9 = 27;
    public static final int F10 = 28;
    public static final int F11 = 29;
    public static final int F12 = 30;
    public static final int NumLock = 31;

    // Printable ASCII characters START
    public static final int Space = 32;
    public static final int ExclamationMark = 33; // !
    public static final int QuotationMark = 34; // "
    public static final int NumberSign = 35; // #
    public static final int Dollar = 36; // $
    public static final int Percent = 37; // %
    public static final int And = 38; // &
    public static final int Apostrophe = 39; // '
    public static final int LeftBracket = 40; // (
    public static final int RightBracket = 41; // )
    public static final int Asterisk = 42; // *
    public static final int Plus = 43; // +
    public static final int Comma = 44; // ,
    public static final int Hyphen = 45; // -
    public static final int Period = 46; // .
    public static final int Slash = 47; // /
    public static final int d0 = 48;
    public static final int d1 = 49;
    public static final int d2 = 50;
    public static final int d3 = 51;
    public static final int d4 = 52;
    public static final int d5 = 53;
    public static final int d6 = 54;
    public static final int d7 = 55;
    public static final int d8 = 56;
    public static final int d9 = 57;
    public static final int Colon = 58; // :
    public static final int Semicolon = 59; // ;
    public static final int LessThan = 60; // <
    public static final int Equal = 61; // =
    public static final int GreaterThan = 62; // >
    public static final int QuestionMark = 63; // ?
    public static final int At = 64; // @
    public static final int A = 65;
    public static final int B = 66;
    public static final int C = 67;
    public static final int D = 68;
    public static final int E = 69;
    public static final int F = 70;
    public static final int G = 71;
    public static final int H = 72;
    public static final int I = 73;
    public static final int J = 74;
    public static final int K = 75;
    public static final int L = 76;
    public static final int M = 77;
    public static final int N = 78;
    public static final int O = 79;
    public static final int P = 80;
    public static final int Q = 81;
    public static final int R = 82;
    public static final int S = 83;
    public static final int T = 84;
    public static final int U = 85;
    public static final int V = 86;
    public static final int W = 87;
    public static final int X = 88;
    public static final int Y = 89;
    public static final int Z = 90;
    public static final int LeftSquareBracket = 91; // [
    public static final int Backslash = 92; // \
    public static final int RightSquareBracket = 93; // ]
    public static final int Caret = 94; // ^
    public static final int Underscore = 95; // _
    public static final int GraveAccent = 42; // `
    /**
     * Small letters are purposely left out, these can easily be calculated
     * by applying an offset of +33 to the capital ones.
     * NEVER DO THIS IN ANY OTHER WAY, YOU WILL WANT TO HURT YOURSELF.
     */
    public static final int LeftCurlyBracket = 123; // {
    public static final int VerticalBar = 124; // |
    public static final int RightCurlyBracket = 125; // }
    public static final int Tilde = 126; // ~

    // Printable ASCII characters END

    public static final int AcuteAccent = 128; // ´
    public static final int Euro = 129; // €
    public static final int Section = 130; // §
    public static final int Degree = 131; // °
    public static final int ae = 131;
    public static final int oe = 132;
    public static final int ue = 133;
    public static final int Ae = 134;
    public static final int Oe = 135;
    public static final int Ue = 136;
    public static final int NUM_Slash = 137;
    public static final int NUM_Asterisk = 138;
    public static final int NUM_Minus = 139;
    public static final int NUM_Plus = 140;
    public static final int NUM_Enter = 141;
    public static final int NUM_Comma = 142;
    public static final int NUM_0 = 143;
    public static final int NUM_1 = 144;
    public static final int NUM_2 = 145;
    public static final int NUM_3 = 146;
    public static final int NUM_4 = 147;
    public static final int NUM_5 = 148;
    public static final int NUM_6 = 149;
    public static final int NUM_7 = 150;
    public static final int NUM_8 = 151;
    public static final int NUM_9 = 152;
    public static final int SharpS = 153;

    // More special characters - Not printable

    public static final int PageUp = 160;
    public static final int PageDown = 161;
    public static final int CapsLock = 162;
    public static final int Print = 163;
    public static final int Roll = 164;
    public static final int Pause = 165;
    public static final int Break = 166;
}
