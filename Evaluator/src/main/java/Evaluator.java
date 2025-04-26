import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {
    private static final String OPERATORS = "+-*/^()";
    private static final String DELIMITER = " ";
    private static final String DIVISION_BY_ZERO_MSG = "Division by zero";
    private static final String INVALID_POSTFIX_MSG = "Invalid postfix expression";
    private static final String INVALID_INFIX_MSG = "Invalid infix expression";
    private static final String UNKNOWN_OPERATOR_MSG = "Unknown operator: ";

    public double evaluate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an expression: ");
        String expression = scanner.nextLine();
        scanner.close();
        return evaluteInfix(expression);
    }

    public double evaluteInfix(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }
        String postfix = convertInfixToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    public double evaluatePostfix(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        Stack<Double> operandStack = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(expression, DELIMITER);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            processToken(token, operandStack);
        }

        validateFinalStackState(operandStack);
        return operandStack.pop();
    }

    private void processToken(String token, Stack<Double> operandStack) {
        if (isOperator(token)) {
            processOperator(token, operandStack);
        } else if (isNumber(token)) {
            operandStack.push(Double.parseDouble(token));
        } else {
            throw new IllegalArgumentException(INVALID_POSTFIX_MSG);
        }
    }

    private void processOperator(String token, Stack<Double> operandStack) {
        if (operandStack.size() < 2) {
            throw new IllegalArgumentException(INVALID_POSTFIX_MSG);
        }
        double secondOperand = operandStack.pop();
        double firstOperand = operandStack.pop();
        operandStack.push(calculateResult(token, firstOperand, secondOperand));
    }

    private void validateFinalStackState(Stack<Double> operandStack) {
        if (operandStack.size() != 1) {
            throw new IllegalArgumentException(INVALID_POSTFIX_MSG);
        }
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String token) {
        return token.length() == 1 && OPERATORS.contains(token);
    }

    private double calculateResult(String operator, double a, double b) {
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) {
                    throw new ArithmeticException(DIVISION_BY_ZERO_MSG);
                }
                yield a / b;
            }
            case "^" -> Math.pow(a, b); // added support for exponentiation
            default -> throw new UnsupportedOperationException(UNKNOWN_OPERATOR_MSG + operator);
        };
    }

    private String convertInfixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(infix, DELIMITER);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            processInfixToken(token, postfix, operatorStack);
        }

        appendRemainingOperators(postfix, operatorStack);
        return postfix.toString();
    }

    private void processInfixToken(String token, StringBuilder postfix, Stack<Character> operatorStack) {
        if (isNumber(token)) {
            appendWithSpace(postfix, token);
        } else if (isOperator(token)) {
            processInfixOperator(token.charAt(0), postfix, operatorStack);
        } else {
            throw new IllegalArgumentException(INVALID_INFIX_MSG + " failed to process: " + token);
        }
    }

    private void processInfixOperator(char operator, StringBuilder postfix, Stack<Character> operatorStack) {
        if (operator == '(') {
            operatorStack.push(operator);
            return;
        }

        if (operator == ')') {
            while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                appendWithSpace(postfix, String.valueOf(operatorStack.pop()));
            }
            if (operatorStack.isEmpty()) {
                throw new IllegalArgumentException(INVALID_INFIX_MSG + " a '(' is missing");
            }
            operatorStack.pop(); // pop the '('
            return;
        }

        while (!operatorStack.isEmpty() && precedence(operatorStack.peek(), operator)) {
            appendWithSpace(postfix, String.valueOf(operatorStack.pop()));
        }

        operatorStack.push(operator);
    }

    private void appendWithSpace(StringBuilder sb, String str) {
        if (!sb.isEmpty()) {
            sb.append(DELIMITER);
        }
        sb.append(str);
    }

    private void appendRemainingOperators(StringBuilder postfix, Stack<Character> operatorStack) {
        while (!operatorStack.isEmpty()) {
            appendWithSpace(postfix, String.valueOf(operatorStack.pop()));
        }
    }

    private boolean precedence(char top, char current) {
        int diff = getPrecedence(top) - getPrecedence(current);
        if (top == '^') return diff > 0; // right associative
        return diff >= 0;
    }

    private int getPrecedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> throw new UnsupportedOperationException(UNKNOWN_OPERATOR_MSG + operator);
        };
    }
}
