package collections;

import devices.StaticV24;
import kernel.memory.MemoryBlock;

public class MemoryBlockList {
    private MemoryBlockListElement _firstElement;
    private MemoryBlockListElement _lastElement;
    private int _elementCount = 0;


    public MemoryBlockList() {
        _firstElement = null;
        _lastElement = null;
    }


    /**
     * Insert a new element into the list.
     * @param data The element to add.
     */
    public void add(MemoryBlock data) {
        MemoryBlockListElement element = new MemoryBlockListElement(data);
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
    public MemoryBlock elementAt(int index) {
        if(_elementCount > 0) {
            MemoryBlockListElement currentElement = _firstElement;
            for(int i = 0; i < index; i++) {
                currentElement = currentElement.next;
            }
            return currentElement.data;
        }
        return null;
    }


    /**
     * Removes the element at the given position.
     * @param index The index to remove at.
     */
    public void removeAt(int index) {
        if(_elementCount > 0) {
            MemoryBlockListElement currentElement = _firstElement;
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


    private static class MemoryBlockListElement {
        MemoryBlock data;
        public MemoryBlockListElement next = null;
        public MemoryBlockListElement previous = null;

        public MemoryBlockListElement(MemoryBlock data) {
            this.data = data;
        }
    }
}
