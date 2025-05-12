import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExpTreeWithVariablesTest {
    @Test
    public void testEvalWithSingleVariable() {
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", 5.0);
        ExpTreeWithVariables tree = new ExpTreeWithVariables("( x + 2 )", vars);
        assertEquals(7.0, tree.eval(), 1e-9);
    }

    @Test
    public void testEvalWithMultipleVariables() {
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", 2.0);
        vars.put("y", 3.0);
        ExpTreeWithVariables tree = new ExpTreeWithVariables("( ( x + y ) * 4 )", vars);
        assertEquals(20.0, tree.eval(), 1e-9);
    }

    @Test
    public void testEvalWithPowerAndVariables() {
        Map<String, Double> vars = new HashMap<>();
        vars.put("a", 2.0);
        vars.put("b", 3.0);
        ExpTreeWithVariables tree = new ExpTreeWithVariables("( a ^ b )", vars);
        assertEquals(8.0, tree.eval(), 1e-9);
    }

    @Test
    public void testEvalWithNegativeVariable() {
        Map<String, Double> vars = new HashMap<>();
        vars.put("z", -10.0);
        ExpTreeWithVariables tree = new ExpTreeWithVariables("( 2 * z )", vars);
        assertEquals(-20.0, tree.eval(), 1e-9);
    }

    @Test
    public void testEvalThrowsOnMissingVariable() {
        Map<String, Double> vars = new HashMap<>();
        ExpTreeWithVariables tree = new ExpTreeWithVariables("( x + 1 )", vars);
        assertThrows(RuntimeException.class, tree::eval);
    }

    @Test
    public void testPreorderTraversalWithVariables() {
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", 1.0);
        vars.put("y", 2.0);
        ExpTreeWithVariables tree = new ExpTreeWithVariables("( x * y )", vars);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        tree.preorder();
        System.setOut(originalOut);
        assertEquals("* x y\n", outContent.toString());
    }

    @Test
    public void testInorderTraversalWithVariables() {
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", 1.0);
        vars.put("y", 2.0);
        ExpTreeWithVariables tree = new ExpTreeWithVariables("( x * y )", vars);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        tree.inorder();
        System.setOut(originalOut);
        assertEquals("( x * y )\n", outContent.toString());
    }

    @Test
    public void testPostorderTraversalWithVariables() {
        Map<String, Double> vars = new HashMap<>();
        vars.put("x", 1.0);
        vars.put("y", 2.0);
        ExpTreeWithVariables tree = new ExpTreeWithVariables("( x * y )", vars);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        tree.postorder();
        System.setOut(originalOut);
        assertEquals("x y *\n", outContent.toString());
    }
} 