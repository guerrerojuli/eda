import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExpTreeTest {

    private ExpTree createExpTree(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        return new ExpTree();
    }

    @Test
    public void testSimpleConstant() {
        ExpTree tree = createExpTree("42");
        // Verify the tree structure
        assertEquals("42", tree.getRoot().getData());
        assertNull(tree.getRoot().getLeft());
        assertNull(tree.getRoot().getRight());
    }

    @Test
    public void testSimpleAddition() {
        ExpTree tree = createExpTree("( 2 + 3 )");
        // Verify the tree structure
        assertEquals("+", tree.getRoot().getData());
        assertEquals("2", tree.getRoot().getLeft().getData());
        assertEquals("3", tree.getRoot().getRight().getData());
    }

    @Test
    public void testComplexExpression() {
        ExpTree tree = createExpTree("( ( 2 + 3 ) * ( 4 - 1 ) )");
        // Verify the tree structure
        assertEquals("*", tree.getRoot().getData());
        assertEquals("+", tree.getRoot().getLeft().getData());
        assertEquals("-", tree.getRoot().getRight().getData());
        assertEquals("2", tree.getRoot().getLeft().getLeft().getData());
        assertEquals("3", tree.getRoot().getLeft().getRight().getData());
        assertEquals("4", tree.getRoot().getRight().getLeft().getData());
        assertEquals("1", tree.getRoot().getRight().getRight().getData());
    }

    @Test
    public void testPowerOperation() {
        ExpTree tree = createExpTree("( 2 ^ 3 )");
        // Verify the tree structure
        assertEquals("^", tree.getRoot().getData());
        assertEquals("2", tree.getRoot().getLeft().getData());
        assertEquals("3", tree.getRoot().getRight().getData());
    }

    @Test
    public void testDivisionOperation() {
        ExpTree tree = createExpTree("( 6 / 2 )");
        // Verify the tree structure
        assertEquals("/", tree.getRoot().getData());
        assertEquals("6", tree.getRoot().getLeft().getData());
        assertEquals("2", tree.getRoot().getRight().getData());
    }

    @Test
    public void testInvalidExpressionMissingParenthesis() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            createExpTree("2 + 3");
        });
    }

    @Test
    public void testInvalidExpressionMissingOperator() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            createExpTree("( 2 3 )");
        });
    }

    @Test
    public void testInvalidExpressionMissingClosingParenthesis() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            createExpTree("( 2 + 3");
        });
    }

    @Test
    public void testInvalidExpressionExtraTokens() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            createExpTree("( 2 + 3 ) 4");
        });
    }

    @Test
    public void testPreorderTraversal() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Create tree with expression "( ( 2 + 3.5 ) * -10 )"
        ExpTree tree = createExpTree("( ( 2 + 3.5 ) * -10 )");
        tree.preorder();

        // Restore System.out
        System.setOut(originalOut);

        // Verify output
        assertEquals("* + 2 3.5 -10\n", outContent.toString());
    }

    @Test
    public void testInorderTraversal() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Create tree with expression "( ( 2 + 3.5 ) * -10 )"
        ExpTree tree = createExpTree("( ( 2 + 3.5 ) * -10 )");
        tree.inorder();

        // Restore System.out
        System.setOut(originalOut);

        // Verify output
        assertEquals("( ( 2 + 3.5 ) * -10 )\n", outContent.toString());
    }

    @Test
    public void testPostorderTraversal() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Create tree with expression "( ( 2 + 3.5 ) * -10 )"
        ExpTree tree = createExpTree("( ( 2 + 3.5 ) * -10 )");
        tree.postorder();

        // Restore System.out
        System.setOut(originalOut);

        // Verify output
        assertEquals("2 3.5 + -10 *\n", outContent.toString());
    }

    @Test
    public void testEmptyTreeTraversals() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Create tree with a single number
        ExpTree tree = createExpTree("42");
        
        // Test all traversals
        tree.preorder();
        tree.inorder();
        tree.postorder();

        // Restore System.out
        System.setOut(originalOut);

        // Verify output
        assertEquals("42\n42\n42\n", outContent.toString());
    }

    @Test
    public void testSingleNodeTraversals() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Create tree with single node
        ExpTree tree = createExpTree("42");
        
        // Test all traversals
        tree.preorder();
        tree.inorder();
        tree.postorder();

        // Restore System.out
        System.setOut(originalOut);

        // Verify output
        assertEquals("42\n42\n42\n", outContent.toString());
    }

    @Test
    public void testEvalSimpleConstant() {
        ExpTree tree = createExpTree("42");
        assertEquals(42.0, tree.eval(), 1e-9);
    }

    @Test
    public void testEvalSimpleAddition() {
        ExpTree tree = createExpTree("( 2 + 3 )");
        assertEquals(5.0, tree.eval(), 1e-9);
    }

    @Test
    public void testEvalComplexExpression() {
        ExpTree tree = createExpTree("( ( 2 + 3 ) * ( 4 - 1 ) )");
        assertEquals(15.0, tree.eval(), 1e-9);
    }

    @Test
    public void testEvalPowerOperation() {
        ExpTree tree = createExpTree("( 2 ^ 3 )");
        assertEquals(8.0, tree.eval(), 1e-9);
    }

    @Test
    public void testEvalDivisionOperation() {
        ExpTree tree = createExpTree("( 6 / 2 )");
        assertEquals(3.0, tree.eval(), 1e-9);
    }

    @Test
    public void testEvalNegativeNumbers() {
        ExpTree tree = createExpTree("( -5 * ( 2 + 3 ) )");
        assertEquals(-25.0, tree.eval(), 1e-9);
    }

    @Test
    public void testEvalDecimals() {
        ExpTree tree = createExpTree("( ( 2 + 3.5 ) * -10 )");
        assertEquals(-55.0, tree.eval(), 1e-9);
    }
} 