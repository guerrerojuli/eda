import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class Evaluator {
    // Constantes: operadores permitidos, delimitador y mensajes de error.
    private static final String OPERATORS = "+-*/^()";
    private static final String DELIMITER = " ";
    private static final String DIVISION_BY_ZERO_MSG = "Division by zero"; // Mensaje para división por cero
    private static final String INVALID_POSTFIX_MSG = "Invalid postfix expression"; // Error en expresión postfija
    private static final String INVALID_INFIX_MSG = "Invalid infix expression"; // Error en expresión infija
    private static final String UNKNOWN_OPERATOR_MSG = "Unknown operator: "; // Mensaje para operador desconocido

    // Método principal que lee y evalúa una expresión en notación infija.
    public double evaluate() {
        Scanner scanner = new Scanner(System.in); // Crea el Scanner para leer de la entrada estándar
        System.out.print("Enter an expression: "); // Solicita la expresión al usuario
        String expression = scanner.nextLine(); // Lee la línea completa
        scanner.close(); // Cierra el Scanner para liberar recursos
        return evaluteInfix(expression); // Convierte y evalúa la expresión en infijo
    }

    // Evalúa una expresión infija validándola, convirtiéndola a postfijo y evaluándola.
    public double evaluteInfix(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            // Verifica que la expresión no sea nula ni vacía.
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }
        String postfix = convertInfixToPostfix(expression); // Convierte la expresión a notación postfija
        return evaluatePostfix(postfix); // Evalúa la expresión postfija y retorna el resultado
    }

    // Evalúa una expresión en notación postfija utilizando una pila.
    public double evaluatePostfix(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            // Verifica que la expresión postfija no sea nula ni vacía.
            throw new IllegalArgumentException("Expression cannot be null or empty");
        }

        Stack<Double> operandStack = new Stack<>(); // Crea una pila para los operandos
        StringTokenizer tokenizer = new StringTokenizer(expression, DELIMITER); // Tokeniza la expresión con espacios

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken(); // Obtiene el siguiente token
            processToken(token, operandStack); // Procesa el token (número o operador)
        }

        validateFinalStackState(operandStack); // Asegura que la pila tenga solo el resultado final
        return operandStack.pop(); // Retorna el resultado final
    }

    // Procesa cada token de la expresión postfija; determina si es número o operador.
    private void processToken(String token, Stack<Double> operandStack) {
        if (isOperator(token)) {
            processOperator(token, operandStack); // Si es operador, lo procesa.
        } else if (isNumber(token)) {
            operandStack.push(Double.parseDouble(token)); // Si es número, lo analiza y lo coloca en la pila.
        } else {
            // Si el token no es válido, lanza error.
            throw new IllegalArgumentException(INVALID_POSTFIX_MSG);
        }
    }

    // Procesa un operador desapilando dos operandos, aplicando la operación y empujando el resultado.
    private void processOperator(String token, Stack<Double> operandStack) {
        if (operandStack.size() < 2) {
            // Verifica que existan al menos dos operandos para operar.
            throw new IllegalArgumentException(INVALID_POSTFIX_MSG);
        }
        double secondOperand = operandStack.pop(); // Extrae el operando derecho
        double firstOperand = operandStack.pop();  // Extrae el operando izquierdo
        operandStack.push(calculateResult(token, firstOperand, secondOperand)); // Calcula y empuja el resultado
    }

    // Valida que la pila tenga únicamente un valor final; de lo contrario, la expresión es inválida.
    private void validateFinalStackState(Stack<Double> operandStack) {
        if (operandStack.size() != 1) {
            throw new IllegalArgumentException(INVALID_POSTFIX_MSG);
        }
    }

    // Determina si el token es numérico.
    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token); // Intenta convertir el token a número
            return true;
        } catch (NumberFormatException e) {
            return false; // Si falla, no es un número
        }
    }

    // Comprueba si el token es un operador permitido (únicamente un carácter y contenido en OPERATORS).
    private boolean isOperator(String token) {
        return token.length() == 1 && OPERATORS.contains(token);
    }

    // Calcula el resultado de la operación aritmética basándose en el operador y los dos operandos.
    private double calculateResult(String operator, double a, double b) {
        return switch (operator) {
            case "+" -> a + b; // Suma
            case "-" -> a - b; // Resta
            case "*" -> a * b; // Multiplicación
            case "/" -> {
                // Antes de dividir, verifica que el divisor no sea cero.
                if (b == 0) {
                    throw new ArithmeticException(DIVISION_BY_ZERO_MSG);
                }
                yield a / b;
            }
            case "^" -> Math.pow(a, b); // Exponenciación
            default -> throw new UnsupportedOperationException(UNKNOWN_OPERATOR_MSG + operator); // Operador desconocido
        };
    }

    // Convierte una expresión de notación infija a notación postfija usando la técnica de la pila.
    private String convertInfixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder(); // Crea el StringBuilder para la expresión postfija
        Stack<Character> operatorStack = new Stack<>(); // Pila para operadores
        StringTokenizer tokenizer = new StringTokenizer(infix, DELIMITER); // Tokeniza la expresión infija

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken(); // Procesa cada token de infijo
            processInfixToken(token, postfix, operatorStack); // Maneja el token según su tipo (número u operador)
        }

        appendRemainingOperators(postfix, operatorStack); // Añade los operadores que queden en la pila
        return postfix.toString(); // Retorna la expresión postfija resultante
    }

    // Procesa cada token de la expresión infija: agrega números a la salida o procesa operadores.
    private void processInfixToken(String token, StringBuilder postfix, Stack<Character> operatorStack) {
        if (isNumber(token)) {
            appendWithSpace(postfix, token); // Si el token es número, lo añade a la salida postfija
        } else if (isOperator(token)) {
            processInfixOperator(token.charAt(0), postfix, operatorStack); // Si es operador, lo procesa
        } else {
            // Token inválido en expresión infija.
            throw new IllegalArgumentException(INVALID_INFIX_MSG + " failed to process: " + token);
        }
    }

    // Procesa operadores en la expresión infija, incluyendo el manejo de paréntesis y la precedencia.
    private void processInfixOperator(char operator, StringBuilder postfix, Stack<Character> operatorStack) {
        if (operator == '(') {
            operatorStack.push(operator); // Si es '(', se empuja a la pila.
            return;
        }

        if (operator == ')') {
            // Si es ')', se extraen operadores hasta encontrar '('.
            while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                appendWithSpace(postfix, String.valueOf(operatorStack.pop()));
            }
            if (operatorStack.isEmpty()) {
                // Si no se encuentra '(', la expresión es inválida.
                throw new IllegalArgumentException(INVALID_INFIX_MSG + " a '(' is missing");
            }
            operatorStack.pop(); // Elimina el '(' de la pila.
            return;
        }

        // Para otros operadores, desapila los que tengan mayor o igual precedencia.
        while (!operatorStack.isEmpty() && precedence(operatorStack.peek(), operator)) {
            appendWithSpace(postfix, String.valueOf(operatorStack.pop()));
        }

        operatorStack.push(operator); // Empuja el operador actual en la pila.
    }

    // Añade el token a la expresión postfija, agregando un espacio si ya existe contenido.
    private void appendWithSpace(StringBuilder sb, String str) {
        if (!sb.isEmpty()) {
            sb.append(DELIMITER);
        }
        sb.append(str);
    }

    // Añade todos los operadores restantes en la pila a la expresión postfija.
    private void appendRemainingOperators(StringBuilder postfix, Stack<Character> operatorStack) {
        while (!operatorStack.isEmpty()) {
            appendWithSpace(postfix, String.valueOf(operatorStack.pop()));
        }
    }

    // Verifica la precedencia: determina si el operador en la cima tiene mayor o igual precedencia que el actual.
    private boolean precedence(char top, char current) {
        int diff = getPrecedence(top) - getPrecedence(current);
        if (top == '^') return diff > 0; // El operador '^' es asociativo por la derecha
        return diff >= 0;
    }

    // Retorna el valor numérico de precedencia para cada operador.
    private int getPrecedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1; // Precedencia para suma y resta
            case '*', '/' -> 2; // Precedencia para multiplicación y división
            case '^' -> 3; // Precedencia para potenciación
            default -> throw new UnsupportedOperationException(UNKNOWN_OPERATOR_MSG + operator);
        };
    }
}

