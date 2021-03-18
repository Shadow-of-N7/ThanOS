package Collections;

public class IntStack {

    private StackElement _topElement = null;
    private int _elementCount = 0;


    public IntStack()
    {

    }


    public void push(int object) {
        _topElement = new StackElement(object);
        ++_elementCount;
    }


    public int pop() {
        if(_elementCount > 0) {
            int temp = _topElement.data;
            _topElement = _topElement.next;

            // This must only be executed if something is in the stack!
            // Otherwise we might end up with a negative count,
            // resulting in the last element being inaccessible forever.
            --_elementCount;
            return temp;
        }
        return 0;
    }


    public int getSize() {
        return _elementCount;
    }


    private class StackElement {
        StackElement next;
        int data;

        public StackElement(int object)
        {
            data = object;
            next = _topElement;
        }
    }
}

