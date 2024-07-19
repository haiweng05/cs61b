import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RadixSortTester {
    private static String[] before = {"aaa", "abc", "a", "aae", "e"};
    private static String[] original = {"aaa", "abc", "a", "aae", "e"};
    private static String[] target = {"a", "aaa", "aae", "abc", "e"};

    @Test
    public void RadixSortTest() {
        assertEquals(RadixSort.sort(before), target);
        assertEquals(before, original);
    }

    public static void main(String[] arg) {
        RadixSortTester r = new RadixSortTester();
        r.RadixSortTest();
    }
}
