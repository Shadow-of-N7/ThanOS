package collections;

import io.Console;

public class Ringbuffer {
    // Serves as empty marking, as null is not permitted
    private final static int INT_MIN = -2147483648;
    private final int[] _buffer;
    private int _readerIndex;
    private int _writerIndex;

    public Ringbuffer(int size) {
        _buffer = new int[size];
        for(int i = 0; i < size; i++) {
            _buffer[i] = INT_MIN;
        }
        _readerIndex = 0;
        _writerIndex = 0;
    }


    public void insert(int data) {
        _buffer[_writerIndex] = data;
        moveWriteReference(1);
    }


    /**
     * Returns the current element and moves the read reference forward.
     * @return Current data element.
     */
    public int next() {
        int data = _buffer[_readerIndex];
        moveReadReference(1);
        return data;
    }


    /**
     * Returns the current element, removes it from the buffer and moves the read reference forward.
     * @return Current data element.
     */
    public int currentAndClear() {
        int data = _buffer[_readerIndex];
        _buffer[_readerIndex] = INT_MIN;
        moveReadReference(1);
        return data;
    }


    /**
     * Returns the element at the given buffer position.
     * @param index The position of the desired element.
     * @return The desired element.
     */
    @SJC.Inline
    public int elementAt(int index) {
        return _buffer[index];
    }


    /**
     * Returns the current element without moving the reader reference.
     * @return Current data element.
     */
    @SJC.Inline
    public int peek() {
        return _buffer[_readerIndex];
    }


    /**
     * Returns the next element without moving the reader reference.
     * @return Current data element.
     */
    public int peekNext() {
        return _buffer[nextReaderIndex()];
    }


    public boolean nextElementEmpty() {
        return _buffer[nextReaderIndex()] == INT_MIN;
    }


    public boolean currentElementEmpty() {
        return _buffer[_readerIndex] == INT_MIN;
    }


    /**
     * Moves the read reference by the given amount. A negative amount results in backward movement.
     * @param steps The amount of steps to move.
     */
    private void moveReadReference(int steps) {
        if(_readerIndex + steps > _buffer.length - 1) {
            _readerIndex = _readerIndex + steps - _buffer.length;
            return;
        }
        if(_readerIndex + steps < 0) {
            _readerIndex = _buffer.length - (steps - _readerIndex);
            return;
        }
        _readerIndex += steps;
    }


    /**
     * Moves the write reference by the given amount. A negative amount results in backward movement.
     * @param steps The amount of steps to move.
     */
    private void moveWriteReference(int steps) {
        if(_writerIndex + steps > _buffer.length - 1) {
            _writerIndex = _writerIndex + steps - _buffer.length;
            return;
        }
        if(_writerIndex + steps < 0) {
            _writerIndex = _buffer.length - (steps - _writerIndex);
            return;
        }
        _writerIndex += steps;
    }


    private int nextReaderIndex() {
        int targetIndex;
        if(_readerIndex + 1 > _buffer.length - 1) {
            targetIndex = _readerIndex + 1 - _buffer.length;
        }
        else
        {
            targetIndex = _readerIndex + 1;
        }
        return targetIndex;
    }
}
