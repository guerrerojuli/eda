import java.util.Stack;
import java.util.StringTokenizer;

public class StringEval {
    private static final String OPERATORS = "+-*/^";
    private static final String DELIMITER = " ";
    private static final String INVALID_POSTFIX_MSG = "Invalid postfix expression";
    private static final String UNKNOWN_OPERATOR_MSG = "Unknown operator: ";

    public String evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        Stack<String> operandStack = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(expression, DELIMITER);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            processToken(token, operandStack);
        }

        validateFinalStackState(operandStack);
        return operandStack.pop();
    }

    private void processToken(String token, Stack<String> operandStack) {
        if (isOperator(token)) {
            processOperator(token, operandStack);
        } else if (isAlpha(token)) {
            operandStack.push(token);
        } else {
            throw new IllegalArgumentException(INVALID_POSTFIX_MSG);
        }
    }

    private boolean isOperator(String token) {
        return token.length() == 1 && OPERATORS.contains(token);
    }

    private boolean isAlpha(String token) {
        return token.matches("[a-zA-Z]+");
    }

    private String calculateResult(String operator, String a, String b) {
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> {
                int index = a.indexOf(b);
                if (index == -1) yield a;
                yield a.substring(0, index) + a.substring(index + b.length());
            }
            case "*" -> {
                StringBuilder result = new StringBuilder();
                int aLength = a.length();
                int bLength = b.length();
                for (int i = 0; i < aLength || i < bLength; i++) {
                    if (i < aLength) {
                        result.append(a.charAt(i));
                    }
                    if (i < bLength) {
                        result.append(b.charAt(i));
                    }
                }
                yield result.toString();

            }
            case "/" -> {
                // Flag array of b chars (a-z A-Z)
                char[] bChars = new char['Z' - 'A' + 1 + 'z' - 'a' + 1];
                for (int i = 0; i < b.length(); i++) {
                    if (b.charAt(i) >= 'A' && b.charAt(i) <= 'Z') {
                        bChars[b.charAt(i) - 'A'] = 1;
                    } else if (b.charAt(i) >= 'a' && b.charAt(i) <= 'z') {
                        bChars[b.charAt(i) - 'a' + 'Z' - 'A' + 1] = 1;
                    }
                }
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < a.length(); i++) {
                    if (a.charAt(i) >= 'A' && a.charAt(i) <= 'Z') {
                        if (bChars[a.charAt(i) - 'A'] == 0) {
                            result.append(a.charAt(i));
                        }
                    } else if (a.charAt(i) >= 'a' && a.charAt(i) <= 'z') {
                        if (bChars[a.charAt(i) - 'a' + 'Z' - 'A' + 1] == 0) {
                            result.append(a.charAt(i));
                        }
                    }
                }

                yield result.toString();
            }
            case "^" -> {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < b.length(); i++) {
                    result.append(a).append(b, 0, i + 1);
                }
                yield result.toString();

            }
            default -> throw new UnsupportedOperationException(UNKNOWN_OPERATOR_MSG + operator);
        };
    }

    private void processOperator(String token, Stack<String> operandStack) {
        if (operandStack.size() < 2) {
            throw new IllegalArgumentException(INVALID_POSTFIX_MSG);
        }
        String secondOperand = operandStack.pop();
        String firstOperand = operandStack.pop();
        operandStack.push(calculateResult(token, firstOperand, secondOperand));
    }

    private void validateFinalStackState(Stack<String> operandStack) {
        if (operandStack.size() != 1) {
            throw new IllegalArgumentException(INVALID_POSTFIX_MSG);
        }
    }
}
