package collections;

public class CharStack {

    private StackElement _topElement = null;
    private int _elementCount = 0;


    public CharStack()
    {

    }


    /**
     * Inserts an element into the stack.
     * @param object The element to place on the stack.
     */
    public void push(char object) {
        _topElement = new StackElement(object);
        ++_elementCount;
    }


    /**
     * Returns the top element of the stack without removing it from there.
     * @return Top element of the stack.
     */
    public char peek() {
        return _topElement.data;
    }


    /**
     * Takes the top element of the stack and returns it.
     * @return Top element of the stack.
     */
    public char pop() {
        if(_elementCount > 0) {
            char temp = _topElement.data;
            _topElement = _topElement.next;

            // This must only be executed if something is in the stack!
            // Otherwise we might end up with a negative count,
            // resulting in the last element being inaccessible forever.
            --_elementCount;
            return temp;
        }
        return ' ';
    }


    /**
     * The current amount of elements on the stack.
     * @return Amount of stack elements.
     */
    public int getSize() {
        return _elementCount;
    }


    private class StackElement {
        StackElement next;
        char data;

        public StackElement(char object)
        {
            data = object;
            next = _topElement;
        }
    }
}
