package collections;

public class CharList {
    private CharListElement _firstElement = null;
    private CharListElement _lastElement = null;
    private int _elementCount = 0;


    /**
     * Insert a new element into the list.
     * @param data The element to add.
     */
    public void add(char data) {
        CharListElement element = new CharListElement(data);
        if(_firstElement == null) {
            _firstElement = element;
        }
        else {
            element.previous = _lastElement;
            _lastElement.next = element;
        }
        _lastElement = element;
        ++_elementCount;
    }


    /**
     * Return the element at the given position.
     * @param index Element index.
     * @return Element at index.
     */
    public char elementAt(int index) {
        if(_elementCount > 0) {
            CharListElement currentElement = _firstElement;
            for(int i = 0; i < index; i++) {
                currentElement = currentElement.next;
            }
            return currentElement.data;
        }
        return 0;
    }


    /**
     * Removes the element at the given position.
     * @param index The index to remove at.
     */
    public void removeAt(int index) {
        if(_elementCount > 0) {
            CharListElement currentElement = _firstElement;
            for(int i = 0; i < index; i++) {
                currentElement = currentElement.next;
            }
            if(currentElement.previous != null) {
                currentElement.previous.next = currentElement.next;
            }
            else {
                _firstElement = currentElement.next;
            }

            if(currentElement.next != null) {
                currentElement.next.previous = currentElement.previous;
            }
            else {
                _lastElement = currentElement.previous;
            }

            --_elementCount;
            return;
        }
    }


    /**
     * Clears the list. Will currently produce a memory leak as there is no GC or likewise.
     */
    public void clear() {
        _firstElement = null;
        _lastElement = null;
        _elementCount = 0;
    }


    public char[] toArray() {
        char[] array = new char[_elementCount];
        for(int i = 0; i < _elementCount; i++) {
            array[i] = elementAt(i);
        }
        return array;
    }


    /**
     * Returns the amount of elements currently stored within the list.
     * @return List length.
     */
    @SJC.Inline
    public int getLength() {
        return _elementCount;
    }


    private static class CharListElement {
        char data;
        public CharListElement next = null;
        public CharListElement previous = null;

        public CharListElement(char data) {
            this.data = data;
        }
    }
}
