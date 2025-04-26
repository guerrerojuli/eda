import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {
    
    @Test
    public void testSpecificCases() {
        // Test cases from the exercise
        assertEquals("BRKL", Metaphone.metaphone("brooklin"));
        assertEquals("BRKL", Metaphone.metaphone("bruqleen"));
        assertEquals("BRKL", Metaphone.metaphone("brooclean"));
        assertEquals("BLKL", Metaphone.metaphone("bluclean"));
        assertEquals("KLN", Metaphone.metaphone("clean"));
    }

    @Test
    public void testUppercaseInput() {
        // Test the same cases with uppercase input to verify case insensitivity
        assertEquals("BRKL", Metaphone.metaphone("BROOKLIN"));
        assertEquals("BRKL", Metaphone.metaphone("BRUQLEEN"));
        assertEquals("BRKL", Metaphone.metaphone("BROOCLEAN"));
        assertEquals("BLKL", Metaphone.metaphone("BLUCLEAN"));
        assertEquals("KLN", Metaphone.metaphone("CLEAN"));
    }
} 