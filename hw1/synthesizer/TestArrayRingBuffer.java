package synthesizer;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<String> arb = new ArrayRingBuffer<>(10);
        arb.enqueue("1");
        arb.enqueue("2");
        assertFalse(arb.isFull());
        assertEquals(arb.peek(), "1");
        arb.enqueue("3");
        arb.enqueue("4");
        assertEquals(arb.dequeue(), "1");
        assertEquals(arb.fillCount(), 3);
    }

    @Test
    public void iteratorTest() {
        ArrayRingBuffer<String> arb = new ArrayRingBuffer<>(10);
        arb.enqueue("1");
        arb.enqueue("2");
        arb.enqueue("3");
        arb.enqueue("4");
        arb.enqueue("5");
        arb.dequeue();
        Iterator<String> iter = arb.iterator();
        assertEquals(iter.next(),"2");
        assertEquals(iter.next(),"3");
        assertEquals(iter.next(),"4");
        assertEquals(iter.next(),"5");
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
