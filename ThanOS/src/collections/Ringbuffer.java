package collections;
//@SJC.IgnoreUnit
public class Ringbuffer {
    private final byte[] _buffer;
    private int _readerIndex;
    private int _writerIndex;

    public Ringbuffer(int size) {
        _buffer = new byte[size];
        _readerIndex = 0;
        _writerIndex = 0;
    }


    public void insert(byte data) {
        _buffer[_writerIndex] = data;
        moveWriteReference(1);
    }


    /**
     * Returns the current element and moves the read reference forward.
     * @return Current data element.
     */
    public byte next() {
        byte data =  _buffer[_readerIndex];
        moveReadReference(1);
        return data;
    }


    /**
     * Returns the current element without moving the reader reference.
     * @return Current data element.
     */
    public byte peek() {
        return _buffer[_readerIndex];
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
        if(_readerIndex - steps < 0) {
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
        if(_writerIndex - steps < 0) {
            _writerIndex = _buffer.length - (steps - _writerIndex);
            return;
        }
        _writerIndex += steps;
    }
}
