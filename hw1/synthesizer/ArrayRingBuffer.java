// TODO: Make sure to make this class a part of the synthesizer package
 package synthesizer;
import java.util.Iterator;

//TODO: Make sure to make this class and all of its methods public
//TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> implements Iterable<T> {
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
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
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
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
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
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update
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
        // TODO: Return the first item. None of your instance variables should change.
        return rb[first];
    }

    private int indexForward(int index) {
        return (index + 1) % capacity;
    }

    private int indexBackward(int index) {
        return (index + capacity - 1) % capacity;
    }
    // TODO: When you get to part 5, implement the needed code to support iteration.

    public class Iterator implements java.util.Iterator<T> {
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
    public Iterator iterator() {
        return new Iterator();
    }

}