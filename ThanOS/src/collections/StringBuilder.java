package collections;

/**
 * Builds strings dynamically, being faster then default string concatenation at the cost of memory.
 */
public class StringBuilder {
    // Contains characters
    private CharList _text;

    public StringBuilder() {
        _text = new CharList();
    }

    /**
     * Adds a string to the text.
     * @param text
     */
    public void add(String text) {
        char[] textArray = text.toCharArray();
        for(int i = 0; i < text.length(); i++) {
            _text.add(textArray[i]);
        }
    }

    /**
     * Adds a character to the text.
     * @param character
     */
    public void add(char character) {
        _text.add(character);
    }

    /**
     * Returns the concatenated string and clears the text buffer.
     * @return
     */
    public String toString() {
        char[] text = _text.toArray();
        clear();
        return new String(text);
    }

    /**
     * Clears the text buffer.
     */
    public void clear() {
        _text = new CharList();
    }
}
