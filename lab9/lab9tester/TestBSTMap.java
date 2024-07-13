package lab9tester;

import static org.junit.Assert.*;

import org.junit.Test;
import lab9.BSTMap;

import java.util.Iterator;

/**
 * Tests by Brendan Hu, Spring 2015, revised for 2018 by Josh Hug
 */
public class TestBSTMap {

    @Test
    public void sanityGenericsTest() {
        try {
            BSTMap<String, String> a = new BSTMap<String, String>();
            BSTMap<String, Integer> b = new BSTMap<String, Integer>();
            BSTMap<Integer, String> c = new BSTMap<Integer, String>();
            BSTMap<Boolean, Integer> e = new BSTMap<Boolean, Integer>();
        } catch (Exception e) {
            fail();
        }
    }

    //assumes put/size/containsKey/get work
    @Test
    public void sanityClearTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1 + i);
            //make sure put is working via containsKey and get
            assertTrue(null != b.get("hi" + i));
            assertTrue(b.get("hi" + i).equals(1 + i));
            assertTrue(b.containsKey("hi" + i));
        }
        assertEquals(455, b.size());
        b.clear();
        assertEquals(0, b.size());
        for (int i = 0; i < 455; i++) {
            assertTrue(null == b.get("hi" + i) && !b.containsKey("hi" + i));
        }
    }

    // assumes put works
    @Test
    public void sanityContainsKeyTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertFalse(b.containsKey("waterYouDoingHere"));
        b.put("waterYouDoingHere", 0);
        assertTrue(b.containsKey("waterYouDoingHere"));
    }

    // assumes put works
    @Test
    public void sanityGetTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(null, b.get("starChild"));
        assertEquals(0, b.size());
        b.put("starChild", 5);
        assertTrue(((Integer) b.get("starChild")).equals(5));
        b.put("KISS", 5);
        assertTrue(((Integer) b.get("KISS")).equals(5));
        assertNotEquals(null, b.get("starChild"));
        assertEquals(2, b.size());
    }

    // assumes put works
    @Test
    public void sanitySizeTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(0, b.size());
        b.put("hi", 1);
        assertEquals(1, b.size());
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
        }
        assertEquals(456, b.size());
    }

    //assumes get/containskey work
    @Test
    public void sanityPutTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("hi", 1);
        assertTrue(b.containsKey("hi"));
        assertTrue(b.get("hi") != null);
    }

    @Test
    public void sanityRemoveTest() {
        BSTMap<String, Integer> b = new BSTMap<>();
        b.put("root", 2);
        b.put("soot", 3);
        b.put("aoot", 1);
        assertEquals((Integer)1, b.remove("aoot"));
        assertEquals((Integer)2, b.remove("root"));
        assertEquals(1, b.size());
        assertEquals((Integer)3, b.remove("soot"));
        assertNull(b.remove("soot"));


        BSTMap<String, Integer> bb = new BSTMap<>();
        assertNull(bb.remove("aaa", 13));
        bb.put("root", 2);
        bb.put("soot", 3);
        bb.put("aoot", 1);
        assertNull(bb.remove("aoot", 2));
        assertEquals((Integer)1, bb.remove("aoot", 1));
        assertNull(bb.remove("soot", 2));
        assertEquals((Integer)2, bb.remove("root"));
        assertEquals(1, bb.size());
        assertEquals((Integer)3, bb.remove("soot"));
        assertNull(bb.remove("soot"));
    }

    public void sanityIteratorTest() {
        BSTMap<String, Integer> b = new BSTMap<>();
        b.put("root", 2);
        b.put("soot", 3);
        b.put("aoot", 1);
        Iterator<String> iter = b.iterator();
        assertTrue(iter.hasNext());
        assertEquals("aoot", iter.next());
        assertTrue(iter.hasNext());
        assertEquals("root", iter.next());
        assertTrue(iter.hasNext());
        assertEquals("soot", iter.next());
        assertFalse(iter.hasNext());
    }
    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestBSTMap.class);
    }
}
