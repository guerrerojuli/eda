import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {
    @Test
    public void testSingleWordEncode() {
        // Test examples from image
        assertEquals("T624", Soundex.representation("threshold"));
        assertEquals("H430", Soundex.representation("hold"));
        assertEquals("Z624", Soundex.representation("zresjoulding"));
        assertEquals("P500", Soundex.representation("phone"));
        assertEquals("F500", Soundex.representation("foun"));
    }

    @Test
    public void testCompareWords() {
        // Test word comparisons from image
        assertEquals(0.0, Soundex.compare("threshold", "hold"), 0.001);
        assertEquals(0.75, Soundex.compare("threshold", "zresjoulding"), 0.001);
        assertEquals(0.75, Soundex.compare("phone", "foun"), 0.001);
    }
}
