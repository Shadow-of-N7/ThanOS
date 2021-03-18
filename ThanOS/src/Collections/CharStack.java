package Collections;

public class CharStack {

    private StackElement _topElement = null;
    private int _elementCount = 0;


    public CharStack()
    {

    }


    public void push(char object) {
        _topElement = new StackElement(object);
        ++_elementCount;
    }


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


    public int getLength() {
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
