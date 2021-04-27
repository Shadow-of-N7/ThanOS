package collections;

import io.Console;

public class ObjectList {
    private ObjectListElement _firstElement = null;
    private ObjectListElement _lastElement = null;
    private int _elementCount = 0;


    /**
     * Insert a new element into the list.
     * @param data The element to add.
     */
    public void add(Object data) {
        ObjectListElement element = new ObjectListElement(data);
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
     * Returns the element at the given position.
     * @param index Element index.
     * @return Element at index.
     */
    public Object elementAt(int index) {
        if(_elementCount > 0) {
            ObjectListElement currentElement = _firstElement;
            for(int i = 0; i < index; i++) {
                currentElement = currentElement.next;
            }
            return currentElement.data;
        }
        return null;
    }


    public void setElementAt(int index, Object object) {
        if(_elementCount > 0) {
            ObjectListElement currentElement = _firstElement;
            for(int i = 0; i < index; i++) {
                currentElement = currentElement.next;
            }
            currentElement.data = object;
        }
    }


    /**
     * Removes the element at the given position.
     * @param index The index to remove at.
     */
    public void removeAt(int index) {
        if(_elementCount > 0) {
            ObjectListElement currentElement = _firstElement;
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


    /**
     * Returns the amount of elements currently stored within the list.
     * @return List length.
     */
    @SJC.Inline
    public int getLength() {
        return _elementCount;
    }


    private static class ObjectListElement {
        Object data;
        public ObjectListElement next = null;
        public ObjectListElement previous = null;

        public ObjectListElement(Object data) {
            this.data = data;
        }
    }
}
