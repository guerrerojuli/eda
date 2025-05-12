import java.util.Map;
import java.util.Scanner;

public class ExpTreeWithVariables implements ExpressionService {
    private Node root;
    private final Map<String, Double> variables;

    public ExpTreeWithVariables(String expression, Map<String, Double> variables) {
        this.variables = variables;
        buildTree(expression);
    }

    private void buildTree(String line) {
        Scanner lineScanner = new Scanner(line).useDelimiter("\\s+");
        root = new Node(lineScanner);
        lineScanner.close();
    }

    @Override
    public void preorder() {
        if (root != null) {
            root.preorder();
            System.out.println();
        }
    }

    @Override
    public void inorder() {
        if (root != null) {
            root.inorder();
            System.out.println();
        }
    }

    @Override
    public void postorder() {
        if (root != null) {
            root.postorder();
            System.out.println();
        }
    }

    @Override
    public double eval() {
        if (root == null) {
            throw new RuntimeException("Árbol vacío");
        }
        return root.eval(variables);
    }

    static final class Node {
        private String data;
        private Node left, right;
        private Scanner lineScanner;

        public Node(Scanner theLineScanner) {
            lineScanner = theLineScanner;
            Node auxi = buildExpression();
            data = auxi.data;
            left = auxi.left;
            right = auxi.right;
            if (lineScanner.hasNext())
                throw new RuntimeException("Bad expression");
        }

        private Node() {}

        private Node buildExpression() {
            Node node = new Node();
            // Check if it's a constant
            if (lineScanner.hasNextDouble()) {
                node.data = lineScanner.next();
                return node;
            }
            // Check if it's a variable (single word, not a parenthesis or operator)
            if (lineScanner.hasNext("[a-zA-Z_][a-zA-Z0-9_]*")) {
                node.data = lineScanner.next();
                return node;
            }
            // Check if it's a parenthesized expression
            if (!lineScanner.hasNext("\\(")) {
                throw new RuntimeException("Expected '(' or constant or variable");
            }
            lineScanner.next(); // consume '('
            node.left = buildExpression();
            if (!lineScanner.hasNext("[+\\-*/^]")) {
                throw new RuntimeException("Expected operator");
            }
            node.data = lineScanner.next();
            node.right = buildExpression();
            if (!lineScanner.hasNext("\\)")) {
                throw new RuntimeException("Expected ')'");
            }
            lineScanner.next(); // consume ')'
            return node;
        }

        private void preorder() {
            System.out.print(data);
            if (left != null || right != null) {
                System.out.print(" ");
            }
            if (left != null) {
                left.preorder();
                if (right != null) System.out.print(" ");
            }
            if (right != null) {
                right.preorder();
            }
        }

        private void inorder() {
            if (left != null && right != null) {
                System.out.print("( ");
                left.inorder();
                System.out.print(" " + data + " ");
                right.inorder();
                System.out.print(" )");
            } else {
                System.out.print(data);
            }
        }

        private void postorder() {
            if (left != null) {
                left.postorder();
                System.out.print(" ");
            }
            if (right != null) {
                right.postorder();
                System.out.print(" ");
            }
            System.out.print(data);
        }

        private double eval(Map<String, Double> variables) {
            // Si es hoja, puede ser número o variable
            if (left == null && right == null) {
                if (data.matches("-?\\d+(\\.\\d+)?")) {
                    return Double.parseDouble(data);
                } else {
                    if (!variables.containsKey(data)) {
                        throw new RuntimeException("Variable no definida: " + data);
                    }
                    return variables.get(data);
                }
            }
            double leftVal = left.eval(variables);
            double rightVal = right.eval(variables);
            switch (data) {
                case "+": return leftVal + rightVal;
                case "-": return leftVal - rightVal;
                case "*": return leftVal * rightVal;
                case "/": return leftVal / rightVal;
                case "^": return Math.pow(leftVal, rightVal);
                default: throw new RuntimeException("Operador desconocido: " + data);
            }
        }
    }
} 