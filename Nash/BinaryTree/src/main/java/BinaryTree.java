import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.Function;


public class BinaryTree<T> implements BinaryTreeService<T> {
	
	private Node<T> root;
	private Scanner inputScanner;
	private int tokenCount;
	private int size;
	private final Class<?> componentType;

	public BinaryTree(String fileName, Class<?> componentType) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, FileNotFoundException {
		 InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);

		 if (is == null)
			 throw new FileNotFoundException(fileName);

		this.componentType = componentType;
		 inputScanner = new Scanner(is);
		 inputScanner.useDelimiter("\\s+");

		 buildTree();
		
		 inputScanner.close();
	}
	
	private void buildTree() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
	
		 Queue<NodeHelper<T>> pendingOps= new LinkedList<>();
		 String token;
		 
		 root = new Node<>();
		 pendingOps.add(new NodeHelper<T>(root, (Node<T> n) -> (n)));
		 
		 while(inputScanner.hasNext()){
			 
			 token = inputScanner.next();

			 NodeHelper<T> aPendingOp = pendingOps.remove();
			 Node<T> currentNode = aPendingOp.getNode();
			 
			 if(token.equals("?") ) {
				// no hace falta poner en null al L o R porque ya esta con null
			 	// reservar el espacio en Queue aunque NULL no tiene hijos para aparear

				pendingOps.add(new NodeHelper<T>(null, null));  // como si hubiera izq
				pendingOps.add(new NodeHelper<T>(null, null));  // como si hubiera der
			 }
			 else{
				 Function<Node<T>, Node<T>> anAction= aPendingOp.getAction();
				 currentNode = anAction.apply(currentNode);
			 
				 // armo la info del izq, der o el root
				 Constructor<?> cons = componentType.getConstructor(String.class);
				 currentNode.data = (T) cons.newInstance(token);
				 size++;

				 // hijos se postergan
				 pendingOps.add(new NodeHelper<>(currentNode, (Node<T> n)->(n.setLeftTree(new Node<>()))));
				 pendingOps.add(new NodeHelper<>(currentNode, (Node<T> n)->(n.setRightTree(new Node<>()))));
			 }
			 tokenCount++;
		 }

		 if (root.data == null)  // no entre al ciclo jamas 
			 root= null;
	}

	@Override
	public void preorder() {
		if(root == null)
			throw new IllegalStateException();
		System.out.println(root.preorder(new StringBuilder()));
	}

	@Override
	public void postorder() {
		if(root==null)
			throw new IllegalStateException();
		System.out.println(root.postorder(new StringBuilder()));
	}

	public void printHierarchy(){
		printHierarchy("", root);
	}

	public void printHierarchy(String initial, Node<T> current){
		//Si estamos en un null, imprimimos y no seguimos
		if(current == null){
			System.out.println(initial + "└── " + "null");
			return;
		}
		//Imprimimos el dato
		System.out.println(initial + "└── " + current.data);

		//Si no es hoja, seguimos
		if (!current.isLeaf()) {
			printHierarchy(initial + "    ", current.left);
			printHierarchy(initial + "    ", current.right);
		}
	}

	public String getTree(){
		//Si estamos en un null, imprimimos y no seguimos
		Queue<Node<T>> queue = new LinkedList<>();
		queue.add(root);
		StringBuilder sb = new StringBuilder();

		int count = tokenCount;

		while (count != 0) {
			Node current = queue.remove();
			if (current == null) {
				sb.append("?\t");
				queue.add(null);
				queue.add(null);
			} else {
				sb.append(current.data).append("\t");
				queue.add(current.left);
				queue.add(current.right);
			}
			count--;
		}
		return sb.toString();
	}

	public void toFile(String name) throws IOException {
		String path = "/Users/nachopedemonte/Documents/Code/ITBA/EDA/Codigos/Unidad 5/BinaryTree/src/main/resources/" + name;
		PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
		writer.print(getTree());
		writer.close();
	}

	public int getHeight(){
		if(root == null)
			return  -1;
		return getHeightRec(root, 0);
	}

	private int getHeightRec(Node<T> node, int height){
		if (node.isLeaf())
			return height;

		int heightLeft = 0;
		int heightRight = 0;

		if (node.left != null)
			heightLeft = getHeightRec(node.left, height + 1);
		if (node.right != null)
			heightRight = getHeightRec(node.right, height + 1);

		return Math.max(heightLeft, heightRight);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof BinaryTree<?> that))
			return false;
		return getTree().equals(that.getTree());
	}

		// hasta el get() no se evalua
	static class Node<T> {
		private T data;
		private Node<T> left;
		private Node<T> right;
		
		public Node<T> setLeftTree(Node<T> aNode) {
			left = aNode;
			return left;
		}

		public Node<T> setRightTree(Node<T> aNode) {
			right = aNode;
			return right;
		}

		private boolean isLeaf() {
			return left == null && right == null;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Node<?> node))
				return false;
			return data.equals(node.data)
					&& (left==null && node.left==null || (left!=null && left.equals(node.left)))
					&& (right==null && node.right==null || (right!=null && right.equals(node.right)));
		}

			private String preorder(StringBuilder s) {
				s.append(data).append(" "); //listar
				if(left!=null)
					left.preorder(s); //preorder izq
				if(right!=null)
					right.preorder(s); //preorder der
				return s.toString();
			}

			private String postorder(StringBuilder s) {
				if(left!=null)
					left.postorder(s);
				if(right!=null)
					right.postorder(s);
				s.append(data).append(" ");
				return s.toString();
			}

	}  // end Node class

	static class NodeHelper<T> {
		
		private Node<T> aNode;
		private Function<Node<T>, Node<T>> anAction;
		
		public NodeHelper(Node<T> aNode, Function<Node<T>, Node<T>> anAction ) {
			this.aNode= aNode;
			this.anAction= anAction;
		}

		public Node<T> getNode() {
			return aNode;
		}
		
		public Function<Node<T>, Node<T>> getAction() {
			return anAction;
		}
		
	}

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		BinaryTree rta = new BinaryTree("data1", String.class);
		rta.printHierarchy();
		rta.toFile("result.txt");
		System.out.println(rta.getHeight());

		BinaryTree original = new BinaryTree("data0_3", String.class);
		original.toFile("mydata0_3");
		BinaryTree copia = new BinaryTree("mydata0_3", String.class);
		System.out.println(original.equals(copia) ); // true
		System.out.println(copia.equals(original) ); // true
		System.out.println(original.equals(original) ); // true
		System.out.println(copia.equals(copia) ); // true
		BinaryTree otro = new BinaryTree("data0_1", String.class);
		System.out.println(original.equals(otro) ); // false
		System.out.println(otro.equals(original) ); // false


	}

}  