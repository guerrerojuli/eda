import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

class EvaluatorTest {
    private final Evaluator evaluator = new Evaluator();

    @Test
    void evaluateSimpleAdditionFromInput() {
        String input = "15 + 3";
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertEquals(18.0, evaluator.evaluate(), 0.001);
        System.setIn(originalIn);
    }

    @Test
    void simpleAdditionEvaluatesCorrectly() {
        assertEquals(5.0, evaluator.evaluatePostfix("2 3 +"), 0.001);
    }

    @Test
    void complexExpressionEvaluatesCorrectly() {
        assertEquals(20.0, evaluator.evaluatePostfix("2 3 + 4 *"), 0.001);
    }

    @Test
    void divisionByZeroThrowsException() {
        ArithmeticException exception = assertThrows(ArithmeticException.class, () ->
                evaluator.evaluatePostfix("5 0 /"));
        assertEquals("Division by zero", exception.getMessage());
    }

    @Test
    void missingOperandThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluatePostfix("3 -"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void tooManyOperandsThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluatePostfix("-3.2 4 5 -"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void invalidOperatorThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluatePostfix("-3 4.5 &"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void negativeNumbersEvaluateCorrectly() {
        assertEquals(-1.0, evaluator.evaluatePostfix("-3 2 +"), 0.001);
    }

    @Test
    void decimalNumbersEvaluateCorrectly() {
        assertEquals(3.5, evaluator.evaluatePostfix("1.5 2 +"), 0.001);
    }

    @Test
    void emptyExpressionThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluatePostfix(""));
    }

    @Test
    void invalidNumberFormatThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluatePostfix("abc 2 +"));
    }

    @Test
    void singleNumberEvaluatesCorrectly() {
        assertEquals(7.0, evaluator.evaluatePostfix("7"), 0.001);
    }

    @Test
    void multipleSpacesBetweenTokensAreHandledCorrectly() {
        assertEquals(9.0, evaluator.evaluatePostfix("2   3   +   4   +"), 0.001);
    }

    @Test
    void expressionWithLeadingAndTrailingSpacesEvaluatesCorrectly() {
        assertEquals(6.0, evaluator.evaluatePostfix("   2 4 +   "), 0.001);
    }

    @Test
    void expressionWithOnlyOperatorsThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluatePostfix("+ - * /"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void expressionWithNonNumericOperandsThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluatePostfix("2 a +"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void expressionWithZeroOperandsThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluatePostfix("+"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void expressionWithNegativeAndPositiveNumbersEvaluatesCorrectly() {
        assertEquals(7.0, evaluator.evaluatePostfix("-3 -4 * -5 +"), 0.001);
    }

    @Test
    void expressionWithLargeNumbersEvaluatesCorrectly() {
        assertEquals(1.0E10, evaluator.evaluatePostfix("1E9 10 *"), 0.001);
    }

    @Test
    void expressionWithMixedDecimalAndIntegerOperandsEvaluatesCorrectly() {
        assertEquals(7.5, evaluator.evaluatePostfix("5 2.5 +"), 0.001);
    }

    @Test
    void expressionWithConsecutiveOperatorsThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluatePostfix("2 3 + - *"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void simpleAdditionEvaluatesCorrectlyInfix() {
        assertEquals(5.0, evaluator.evaluteInfix("2 + 3"), 0.001);
    }

    @Test
    void complexExpressionEvaluatesCorrectlyInfix() {
        // Infix: 2 + 3 * 4 = 2 + (3*4) = 14, due to multiplication precedence
        assertEquals(14.0, evaluator.evaluteInfix("2 + 3 * 4"), 0.001);
    }

    @Test
    void divisionByZeroThrowsExceptionInfix() {
        ArithmeticException exception = assertThrows(ArithmeticException.class, () ->
                evaluator.evaluteInfix("5 / 0"));
        assertEquals("Division by zero", exception.getMessage());
    }

    @Test
    void missingOperandThrowsExceptionInfix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluteInfix("3 -"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void singleNumberEvaluatesCorrectlyInfix() {
        assertEquals(7.0, evaluator.evaluteInfix("7"), 0.001);
    }

    @Test
    void negativeNumbersEvaluateCorrectlyInfix() {
        assertEquals(-1.0, evaluator.evaluteInfix("-3 + 2"), 0.001);
    }

    @Test
    void decimalNumbersEvaluateCorrectlyInfix() {
        assertEquals(3.5, evaluator.evaluteInfix("1.5 + 2"), 0.001);
    }

    @Test
    void multipleSpacesBetweenTokensAreHandledCorrectlyInfix() {
        assertEquals(9.0, evaluator.evaluteInfix("2   +   3   +   4"), 0.001);
    }

    @Test
    void expressionWithLeadingAndTrailingSpacesEvaluatesCorrectlyInfix() {
        assertEquals(6.0, evaluator.evaluteInfix("   2 + 4   "), 0.001);
    }

    @Test
    void expressionWithOnlyOperatorsThrowsExceptionInfix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluteInfix("+ - * /"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void expressionWithNonNumericOperandsThrowsExceptionInfix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluteInfix("2 a +"));
        assertEquals("Invalid infix expression failed to process: a", exception.getMessage());
    }

    @Test
    void expressionWithZeroOperandsThrowsExceptionInfix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluteInfix("+"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void expressionWithConsecutiveOperatorsThrowsExceptionInfix() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                evaluator.evaluteInfix("2 + + *"));
        assertEquals("Invalid postfix expression", exception.getMessage());
    }

    @Test
    void exponentiationEvaluatesCorrectlyInPostfix() {
        assertEquals(8.0, evaluator.evaluatePostfix("2 3 ^"), 0.001);
    }

    @Test
    void negativeExponentEvaluatesCorrectlyPostfix() {
        assertEquals(0.125, evaluator.evaluatePostfix("2 -3 ^"), 0.001);
    }

    @Test
    void negativeExponentEvaluatesCorrectlyInfix() {
        assertEquals(0.125, evaluator.evaluteInfix("2 ^ -3"), 0.001);
    }

    @Test
    void scientificNotationEvaluatesCorrectlyPostfix() {
        assertEquals(110.0, evaluator.evaluatePostfix("1E2 1E1 +"), 0.001);
    }

    @Test
    void scientificNotationEvaluatesCorrectlyInfix() {
        assertEquals(110.0, evaluator.evaluteInfix("1E2 + 1E1"), 0.001);
    }

    @Test
    void leadingZerosEvaluatesCorrectlyPostfix() {
        assertEquals(5.0, evaluator.evaluatePostfix("02 03 +"), 0.001);
    }

    @Test
    void leadingZerosEvaluatesCorrectlyInfix() {
        assertEquals(5.0, evaluator.evaluteInfix("02 + 03"), 0.001);
    }

    @Test
    void rightAssociativeExponentiationEvaluatesCorrectlyInfix() {
        // Evaluates as: 2 ^ (3 ^ 2) = 2 ^ 9 = 512
        assertEquals(512.0, evaluator.evaluteInfix("2 ^ 3 ^ 2"), 0.001);
    }

    @Test
    void testPrivateIsNumber() throws Exception {
        Method isNumber = Evaluator.class.getDeclaredMethod("isNumber", String.class);
        isNumber.setAccessible(true);
        Evaluator evaluator = new Evaluator();
        assertTrue((Boolean) isNumber.invoke(evaluator, "123.45"));
        assertFalse((Boolean) isNumber.invoke(evaluator, "abc"));
    }

    @Test
    void testPrivateIsOperator() throws Exception {
        Method isOperator = Evaluator.class.getDeclaredMethod("isOperator", String.class);
        isOperator.setAccessible(true);
        Evaluator evaluator = new Evaluator();
        assertTrue((Boolean) isOperator.invoke(evaluator, "+"));
        assertFalse((Boolean) isOperator.invoke(evaluator, "++"));
    }

    @Test
    void testPrivateCalculateResult() throws Exception {
        Method calculateResult = Evaluator.class.getDeclaredMethod("calculateResult", String.class, double.class, double.class);
        calculateResult.setAccessible(true);
        Evaluator evaluator = new Evaluator();
        assertEquals(8.0, (double) calculateResult.invoke(evaluator, "+", 3.0, 5.0), 0.001);
        assertEquals(2.0, (double) calculateResult.invoke(evaluator, "-", 5.0, 3.0), 0.001);
        assertEquals(15.0, (double) calculateResult.invoke(evaluator, "*", 3.0, 5.0), 0.001);
        assertEquals(4.0, (double) calculateResult.invoke(evaluator, "/", 8.0, 2.0), 0.001);
        Exception ex = assertThrows(Exception.class, () ->
            calculateResult.invoke(evaluator, "/", 5.0, 0.0));
        assertTrue(ex.getCause().getMessage().contains("Division by zero"));
    }

    @Test
    void testPrivateGetPrecedence() throws Exception {
        Method getPrecedence = Evaluator.class.getDeclaredMethod("getPrecedence", char.class);
        getPrecedence.setAccessible(true);
        Evaluator evaluator = new Evaluator();
        // Updated expected precedence values according to Evaluator.java
        assertEquals(0, ((Integer)getPrecedence.invoke(evaluator, '(')).intValue());
        assertEquals(0, ((Integer)getPrecedence.invoke(evaluator, ')')).intValue());
        assertEquals(1, ((Integer)getPrecedence.invoke(evaluator, '+')).intValue());
        assertEquals(1, ((Integer)getPrecedence.invoke(evaluator, '-')).intValue());
        assertEquals(2, ((Integer)getPrecedence.invoke(evaluator, '*')).intValue());
        assertEquals(2, ((Integer)getPrecedence.invoke(evaluator, '/')).intValue());
        assertEquals(3, ((Integer)getPrecedence.invoke(evaluator, '^')).intValue());
    }

    @Test
    void testPrivateConvertInfixToPostfix() throws Exception {
        Method convertInfixToPostfix = Evaluator.class.getDeclaredMethod("convertInfixToPostfix", String.class);
        convertInfixToPostfix.setAccessible(true);
        Evaluator evaluator = new Evaluator();
        // Infix: "2 + 3 * 4" should give "2 3 4 * +"
        String result = (String) convertInfixToPostfix.invoke(evaluator, "2 + 3 * 4");
        assertEquals("2 3 4 * +", result);
    }

    @Test
    void testPrivateAppendWithSpace() throws Exception {
        Method appendWithSpace = Evaluator.class.getDeclaredMethod("appendWithSpace", StringBuilder.class, String.class);
        appendWithSpace.setAccessible(true);
        StringBuilder sb = new StringBuilder();
        Evaluator evaluator = new Evaluator();
        // First append: no space
        appendWithSpace.invoke(evaluator, sb, "token1");
        // Second append: should have a leading space
        appendWithSpace.invoke(evaluator, sb, "token2");
        assertEquals("token1 token2", sb.toString());
    }

    @Test
    void complexExpressionWithMultipleOperatorsEvaluatesCorrectly() throws Exception {
        String infix = "3 + 10 * 2 / 1";
        String expectedPostfix = "3 10 2 * 1 / +";

        Method convertInfixToPostfix = Evaluator.class.getDeclaredMethod("convertInfixToPostfix", String.class);
        convertInfixToPostfix.setAccessible(true);
        String actualPostfix = (String) convertInfixToPostfix.invoke(evaluator, infix);

        assertEquals(expectedPostfix, actualPostfix);
        assertEquals(23.0, evaluator.evaluteInfix(infix), 0.001);
    }

    @Test
    void exponentWithSubtractionAndMultiplicationEvaluatesCorrectly() throws Exception {
        String infix = "13 ^ 2 - 1 * 7";
        String expectedPostfix = "13 2 ^ 1 7 * -";

        Method convertInfixToPostfix = Evaluator.class.getDeclaredMethod("convertInfixToPostfix", String.class);
        convertInfixToPostfix.setAccessible(true);
        String actualPostfix = (String) convertInfixToPostfix.invoke(evaluator, infix);

        assertEquals(expectedPostfix, actualPostfix);
        assertEquals(162.0, evaluator.evaluteInfix(infix), 0.001);
    }

    @Test
    void chainedExponentsWithSubtractionEvaluatesCorrectly() throws Exception {
        String infix = "5 ^ 2 ^ 3 - 1";
        String expectedPostfix = "5 2 3 ^ ^ 1 -";

        Method convertInfixToPostfix = Evaluator.class.getDeclaredMethod("convertInfixToPostfix", String.class);
        convertInfixToPostfix.setAccessible(true);
        String actualPostfix = (String) convertInfixToPostfix.invoke(evaluator, infix);

        assertEquals(expectedPostfix, actualPostfix);
        assertEquals(390624.0, evaluator.evaluteInfix(infix), 0.001);
    }

    @Test
    void expressionsWithParenthesesEvaluateCorrectly() throws Exception {
        String[][] testCases = {
                {"( 2 + 3 ) * 4", "2 3 + 4 *", "20.0"},
                {"2 + ( 3 * 4 )", "2 3 4 * +", "14.0"},
                {"( ( 2 + 3 ) * 4 ) ^ 2", "2 3 + 4 * 2 ^", "400.0"},
                {"( 5 + 3 ) * ( 2 + 4 )", "5 3 + 2 4 + *", "48.0"}
        };

        Method convertInfixToPostfix = Evaluator.class.getDeclaredMethod("convertInfixToPostfix", String.class);
        convertInfixToPostfix.setAccessible(true);

        for (String[] testCase : testCases) {
            String infix = testCase[0];
            String expectedPostfix = testCase[1];
            double expectedResult = Double.parseDouble(testCase[2]);

            String actualPostfix = (String) convertInfixToPostfix.invoke(evaluator, infix);
            assertEquals(expectedPostfix, actualPostfix);
            assertEquals(expectedResult, evaluator.evaluteInfix(infix), 0.001);
        }
    }
}
