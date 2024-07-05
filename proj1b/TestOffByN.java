import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    static OffByN  offbyn = new OffByN(5);

    @Test
    public void testEqualChars() {
        assertTrue(offbyn.equalChars('f', 'a'));
        assertTrue(offbyn.equalChars('b', 'g'));
        assertFalse(offbyn.equalChars('a', 'b'));
    }
}
