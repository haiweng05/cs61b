package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first = 0;
        last = 0;
        rb = (T[]) new Object[capacity];
        this.capacity = capacity;
        this.fillCount = 0;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (isFull()) {
            RuntimeException exp = new RuntimeException("Ring Buffer Overflow");
            throw(exp);
        }
        rb[last] = x;
        last = indexForward(last);
        fillCount++;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (isEmpty()) {
            RuntimeException exp = new RuntimeException("Ring Buffer Underflow");
            throw(exp);
        }
        T tp = rb[first];
        rb[first] = null;
        first = indexForward(first);
        fillCount--;
        return tp;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()) {
            RuntimeException exp = new RuntimeException("Ring Buffer Underflow");
            throw(exp);
        }
        return rb[first];
    }

    private int indexForward(int index) {
        return (index + 1) % capacity;
    }

    private int indexBackward(int index) {
        return (index + capacity - 1) % capacity;
    }

    private class Iterator implements java.util.Iterator<T> {
        private int ptr;
        public Iterator() {
            ptr = first;
        }
        public boolean hasNext() {
            return (ptr != last);
        }
        public T next() {
            T returnItem = rb[ptr];
            ptr = indexForward(ptr);
            return returnItem;
        }
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new Iterator();
    }

}
