import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class StringEvalTest {
    // ...existing code if any...

    // Test operator '+': concatenation
    @Test
    public void testConcatenation() {
        StringEval eval = new StringEval();
        String result = eval.evaluate("AAAAA BBBBB +");
        assertEquals("AAAAABBBBB", result);
    }

    // Test operator '-': deletion of first occurrence of substring
    @Test
    public void testSubstringDeletion() {
        StringEval eval = new StringEval();
        String result = eval.evaluate("AAAAABBCCBB BB -");
        assertEquals("AAAAACCBB", result);
    }

    // Test operator '*': interleaving characters
    @Test
    public void testInterleaving() {
        StringEval eval = new StringEval();
        String result = eval.evaluate("AAA BBBBB *");
        assertEquals("ABABABBB", result);
    }

    // Test operator '/': deletion of characters (all occurrences)
    @Test
    public void testDeletionOfCharacters() {
        StringEval eval = new StringEval();
        String result = eval.evaluate("AAAAABBBCCCDDDAAA AB /");
        assertEquals("CCCDDD", result);
    }

    // Test operator '^': special interleaving
    @Test
    public void testSpecialInterleaving() {
        StringEval eval = new StringEval();
        String result = eval.evaluate("EE ABCD ^");
        assertEquals("EEAEEABEEABCEEABCD", result);
    }

    // Test combined example 1
    @Test
    public void testExample1() {
        StringEval eval = new StringEval();
        String result = eval.evaluate("AA BB CC DEF ^ * AE / + BC -");
        assertEquals("AABCDCCDCCDF", result);
    }

    // Test combined example 2
    @Test
    public void testExample2() {
        StringEval eval = new StringEval();
        String result = eval.evaluate("HOLA QUE + TAL COMO ^ ESTAS / BIEN * + BIEN -");
        assertEquals("HOLAQUELBCILECNOLCOMLCOMO", result);
    }
}
