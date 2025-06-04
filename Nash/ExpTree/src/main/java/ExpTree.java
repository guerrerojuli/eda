import java.util.Scanner;


public class ExpTree implements ExpressionService {

	private Node root;

	public ExpTree() {
	    System.out.print("Introduzca la expresión en notación infija con todos los paréntesis y blancos: ");

		// token analyzer
	    Scanner inputScanner = new Scanner(System.in).useDelimiter("\\n");
	    String line = inputScanner.nextLine();
	    inputScanner.close();

	    buildTree(line);
	}

	public ExpTree(String string) {
		buildTree(string);
	}
	
	private void buildTree(String line) {	
		  // space separator among tokens
		  Scanner lineScanner = new Scanner(line).useDelimiter("\\s+");
		  root = new Node(lineScanner);
		  lineScanner.close();
	}

	@Override
	public void preorder() {
		if(root==null)
			throw new IllegalStateException();
		System.out.println(root.preOrder(new StringBuilder()));
	}

	@Override
	public void inorder() {
		if(root==null)
			throw new IllegalStateException();
		System.out.println(root.inOrder(new StringBuilder()));
	}

	@Override
	public void postorder() {
		if(root==null)
			throw new IllegalStateException();
		System.out.println(root.postOrder(new StringBuilder()));
	}

	@Override
	public double eval() {
		return evalRec(root);
	}

	private double evalRec(Node node) {
		if (node.left == null || node.right == null)
			return Utils.getDoubleConstant(node.data);

		switch (node.data) {
			case "+" -> {
				return evalRec(node.left) + evalRec(node.right);
			}
			case "-" -> {
				return evalRec(node.left) - evalRec(node.right);
			}
			case "*" -> {
				return evalRec(node.left) * evalRec(node.right);
			}
			case "/" -> {
				return evalRec(node.left) / evalRec(node.right);
			}
			case "^" -> {
				return Math.pow(evalRec(node.left), evalRec(node.right));
			}
			default -> throw new IllegalArgumentException("Not a valid operand");
		}
	}

	@Override
	public String toString() {
		return root.toString();
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
				throw new RuntimeException("Incorrect expression format");
		}

		private Node() 	{
		}
		
	
		private Node buildExpression() {
			Node ret = new Node();
			//Regla 1: E -> ( E op E )
			if(lineScanner.hasNext("\\(") ) {
				lineScanner.next();
				ret.left= buildExpression();

				//Operator
				if (!lineScanner.hasNext())
					throw new OperandException("No operands found");

				ret.data = lineScanner.next();
				if (!Utils.isOperator(ret.data))
					throw new OperandException("Invalid operator");

				//Subexpresión
				ret.right = buildExpression();

				//Esperamos un )
				if (lineScanner.hasNext("\\)"))
					lineScanner.next();	//Lo consumimos
				else
					throw new IncorrectParenthesisException("Invalid parenthesis");

				return ret;
			}

			//Regla 2: E -> cte
			if (!lineScanner.hasNext())
				throw new OperandException("Missing operand");

			ret.data = lineScanner.next();
			if (!Utils.isConstant(ret.data))
				throw new OperandException("Invalid operand %s".formatted(ret.data));

			return ret;
		}

		private String preOrder(StringBuilder s){
			s.append(data).append(" ");
			if(left != null)
				left.preOrder(s);
			if(right!= null)
				right.preOrder(s);
			return s.toString();
		}

		private String inOrder(StringBuilder s){
			if(left != null) {
				s.append("( ");
				left.inOrder(s);
			}
			s.append(data).append(" ");
			if(right != null){
				right.inOrder(s);
				s.append(")");
			}
			return s.toString();
		}

		private String postOrder(StringBuilder s){
			if(left != null)
				left.postOrder(s);
			if(right!= null)
				right.postOrder(s);
			s.append(data).append(" ");
			return s.toString();
		}

		@Override
		public String toString(){
			StringBuilder buffer = new StringBuilder();
			print(buffer, "", "");
			return buffer.toString();
		}

		private void print(StringBuilder buffer, String prefix, String childrenPrefix){
			buffer.append(prefix);
			buffer.append(data);
			buffer.append('\n');
			if(left!=null)
				left.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
			if(right!=null)
				right.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
		}
	}  // end Node class
	
	// hasta que armen los testeos
	public static void main(String[] args) {
		ExpressionService myExp = new ExpTree();
		System.out.println(myExp);
		myExp.preorder();
		myExp.inorder();
		myExp.postorder();

		System.out.println("Second Tree Test: ");
		ExpressionService myExp2 = new ExpTree("(  (  2 + 3.5 ) * -10 )\n");
		System.out.println(myExp2.eval());

	}

}  // end ExpTree class
