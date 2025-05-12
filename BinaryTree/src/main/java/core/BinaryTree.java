package core;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.Function;


public class BinaryTree implements BinaryTreeService {
	
	private Node root;
	private Scanner inputScanner;
	private int tokenCount;
	private int size;

	public BinaryTree(String fileName) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, FileNotFoundException {
		 InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);

		 if (is == null)
			 throw new FileNotFoundException(fileName);

		 inputScanner = new Scanner(is);
		 inputScanner.useDelimiter("\\s+");

		 buildTree();
		
		 inputScanner.close();
	}
	
	private void buildTree() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
	
		 Queue<NodeHelper> pendingOps= new LinkedList<>();
		 String token;
		 
		 root = new Node();
		 pendingOps.add(new NodeHelper(root, (Node n) -> (n)));
		 
		 while(inputScanner.hasNext()){
			 
			 token = inputScanner.next();

			 NodeHelper aPendingOp = pendingOps.remove();
			 Node currentNode = aPendingOp.getNode();
			 
			 if(token.equals("?") ) {
				// no hace falta poner en null al L o R porque ya esta con null
			 	// reservar el espacio en Queue aunque NULL no tiene hijos para aparear

				pendingOps.add(new NodeHelper(null, null));  // como si hubiera izq
				pendingOps.add(new NodeHelper(null, null));  // como si hubiera der
			 }
			 else{
				 Function<Node, Node> anAction= aPendingOp.getAction();
				 currentNode = anAction.apply(currentNode);
			 
				 // armo la info del izq, der o el root
				 currentNode.data = token;
				 size++;

				 // hijos se postergan
				 pendingOps.add(new NodeHelper(currentNode, (Node n)->(n.setLeftTree(new Node()))));
				 pendingOps.add(new NodeHelper(currentNode, (Node n)->(n.setRightTree(new Node()))));
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

	public void printHierarchy(String initial, Node current){
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
		Queue<Node> queue = new LinkedList<>();
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
		// Construct path relative to project root (assuming standard structure)
		String relativePath = "src/main/resources/" + name;
		// Ensure the directory exists (optional, but good practice)
		File outputFile = new File(relativePath);
		outputFile.getParentFile().mkdirs(); // Create parent directories if they don't exist

		PrintWriter writer = new PrintWriter(outputFile, StandardCharsets.UTF_8);
		writer.print(getTree());
		writer.close();
	}

	public int getHeight(){
		if(root == null)
			return  -1;
		return getHeightRec(root, 0);
	}

	private int getHeightRec(Node node, int height){
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
		if (!(o instanceof BinaryTree that))
			return false;
		return getTree().equals(that.getTree());
	}

		// hasta el get() no se evalua
	static class Node {
		private String data;
		private Node left;
		private Node right;
		
		public Node setLeftTree(Node aNode) {
			left = aNode;
			return left;
		}

		public Node setRightTree(Node aNode) {
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
			if (!(o instanceof Node node))
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

	static class NodeHelper {
		
		private Node aNode;
		private Function<Node, Node> anAction;
		
		public NodeHelper(Node aNode, Function<Node, Node> anAction ) {
			this.aNode= aNode;
			this.anAction= anAction;
		}

		public Node getNode() {
			return aNode;
		}
		
		public Function<Node, Node> getAction() {
			return anAction;
		}
		
	}

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		BinaryTree rta = new BinaryTree("data1");
		rta.printHierarchy();
		rta.toFile("result.txt");
		System.out.println(rta.getHeight());

		BinaryTree original = new BinaryTree("data0_3");
		original.toFile("mydata0_3");
		BinaryTree copia = new BinaryTree("mydata0_3");
		System.out.println(original.equals(copia) ); // true
		System.out.println(copia.equals(original) ); // true
		System.out.println(original.equals(original) ); // true
		System.out.println(copia.equals(copia) ); // true
		BinaryTree otro = new BinaryTree("data0_1");
		System.out.println(original.equals(otro) ); // false
		System.out.println(otro.equals(original) ); // false


	}

}