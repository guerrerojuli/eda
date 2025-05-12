import java.util.Scanner;

public class ExpTree implements ExpressionService {

	private Node root;

	public ExpTree() {
	    //System.out.print("Introduzca la expresin en notacin infija con todos los parntesis y blancos: ");

		// token analyzer
	    Scanner inputScanner = new Scanner(System.in).useDelimiter("\\n");
	    String line= inputScanner.nextLine();
	    inputScanner.close();

	    buildTree(line);
	}
	
	// Getter for testing purposes
	public Node getRoot() {
		return root;
	}
	
	private void buildTree(String line) {	
		  // space separator among tokens
		  Scanner lineScanner = new Scanner(line).useDelimiter("\\s+");
		  root= new Node(lineScanner);
		  lineScanner.close();
	}
	


	
	static final class Node {
		private String data;
		private Node left, right;
		
		private Scanner lineScanner;

		public Node(Scanner theLineScanner) {
			lineScanner= theLineScanner;
			
			Node auxi = buildExpression();
			data= auxi.data;
			left= auxi.left;
			right= auxi.right;
			
			if (lineScanner.hasNext() ) 
				throw new RuntimeException("Bad expression");
		}
		
		private Node() 	{
		}
		
		// Getters for testing purposes
		public String getData() {
			return data;
		}
		
		public Node getLeft() {
			return left;
		}
		
		public Node getRight() {
			return right;
		}
		
		private Node buildExpression() {
			Node node = new Node();
			
			// Check if it's a constant
			if (lineScanner.hasNextDouble()) {
				node.data = lineScanner.next();
				return node;
			}
			
			// Check if it's a parenthesized expression
			if (!lineScanner.hasNext("\\(")) {
				throw new RuntimeException("Expected '(' or constant");
			}
			lineScanner.next(); // consume '('
			
			// Build left subtree
			node.left = buildExpression();
			
			// Check operator
			if (!lineScanner.hasNext("[+\\-*/^]")) {
				throw new RuntimeException("Expected operator");
			}
			node.data = lineScanner.next();
			
			// Build right subtree
			node.right = buildExpression();
			
			// Check closing parenthesis
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

		private double eval() {
			// Si es hoja, debe ser un número
			if (left == null && right == null) {
				return Double.parseDouble(data);
			}
			// Si es operador
			double leftVal = left.eval();
			double rightVal = right.eval();
			switch (data) {
				case "+": return leftVal + rightVal;
				case "-": return leftVal - rightVal;
				case "*": return leftVal * rightVal;
				case "/": return leftVal / rightVal;
				case "^": return Math.pow(leftVal, rightVal);
				default: throw new RuntimeException("Operador desconocido: " + data);
			}
		}
	}  // end Node class

	
	
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
		return root.eval();
	}

}  // end ExpTree class
