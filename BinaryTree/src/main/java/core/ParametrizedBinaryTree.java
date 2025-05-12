package core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.Function;

public class ParametrizedBinaryTree<T> implements ParametrizedBinaryTreeService<T> {
    private Node<T> root;

    private final Scanner inputScanner;

    private int tokenCount;

    public ParametrizedBinaryTree(String fileName, Class<T> theClass) throws IllegalArgumentException, SecurityException, FileNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);

        if (is == null)
            throw new FileNotFoundException(fileName);

        inputScanner = new Scanner(is);
        inputScanner.useDelimiter("\\s+");

        buildTree(theClass);

        inputScanner.close();
    }

    private void buildTree(Class<T> theClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {	
	
        Queue<NodeHelper<T>> pendingOps= new LinkedList<>();
        String token;
        
        root= new Node<>();
        pendingOps.add(new NodeHelper<>(root, NodeHelper.Action.CONSUMIR));
        
        while(inputScanner.hasNext()) {
            
            token= inputScanner.next();

            NodeHelper<T> aPendingOp = pendingOps.remove();
            Node<T> currentNode = aPendingOp.getNode();
            
            if ( token.equals("?") ) {
                // no hace falta poner en null al L o R porque ya esta con null
                
            // reservar el espacio en Queue aunque NULL no tiene hijos para aparear
               pendingOps.add( new NodeHelper<>(null, NodeHelper.Action.CONSUMIR) );  // como si hubiera izq
               pendingOps.add( new NodeHelper<>(null, NodeHelper.Action.CONSUMIR) );  // como si hubiera der
            }
            else {
                switch (aPendingOp.getAction()) 
                {
                    case LEFT: 
                        currentNode = currentNode.setLeftTree(new Node<>());
                        break;
                    case RIGHT:
                        currentNode = currentNode.setRightTree(new Node<>());
                        break;
                    default: // CONSUMIR (for root)
                        // currentNode is already root, no action needed to set it.
                        break;
                }
                
            
                // armo la info del izq, der o el root
                currentNode.data = theClass.getConstructor(String.class).newInstance(token);

                
                // hijos se postergan
                pendingOps.add(new NodeHelper<>(currentNode, NodeHelper.Action.LEFT));
                pendingOps.add(new NodeHelper<>(currentNode, NodeHelper.Action.RIGHT));
            }
   
                
        }
           
        if (root.data == null)  // no entre al ciclo jamas 
            root= null;
        
    }

    private void buildTree2(Class<T> theClass) throws IllegalArgumentException, SecurityException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Queue<NodeHelper2<T>> pendingOps = new LinkedList<>();
        String token;

        root = new Node<>();
        pendingOps.add(new NodeHelper2<>(root, (Node<T> n) -> (n)));

        while (inputScanner.hasNext()) {

            token = inputScanner.next();

            NodeHelper2<T> aPendingOp = pendingOps.remove();
            Node<T> currentNode = aPendingOp.getNode();

            if (token.equals("?")) {
                // no hace falta poner en null al L o R porque ya esta con null

                // reservar el espacio en Queue aunque NULL no tiene hijos para aparear
                pendingOps.add(new NodeHelper2<>(null, (Node<T> n) -> (n)));  // como si hubiera izq
                pendingOps.add(new NodeHelper2<>(null, (Node<T> n) -> (n)));  // como si hubiera der
            } else {
                Function<Node<T>, Node<T>> anAction = aPendingOp.getAction();
                currentNode = anAction.apply(currentNode);

                // armo la info del izq, der o el root
                currentNode.data = theClass.getConstructor(String.class).newInstance(token);

                // hijos se postergan
                pendingOps.add(new NodeHelper2<>(currentNode, (Node<T> n) -> n.setLeftTree(new Node<>())));
                pendingOps.add(new NodeHelper2<>(currentNode, (Node<T> n) -> n.setRightTree(new Node<>())));
            }
            tokenCount++;

        }

        if (root.data == null)  // no entre al ciclo jamás
            root = null;
    }

    @Override
    public void preorder() {
        if (root != null) {
            root.preorder();
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
    public void printHierarchy() {
        if (root != null) {
            root.printHierarchy("");
            System.out.println();
        }
    }

    @Override
    public int getHeight() {
        if (root == null)
            return -1;
        return getHeightRec(root, 0);
    }

    private int getHeightIter() {
        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(root);
        int height = -1;

        while (!queue.isEmpty()) {
            int size = queue.size();
            height++;

            for (int i = 0; i < size; i++) {
                Node<T> node = queue.remove();

                if (node.left != null)
                    queue.add(node.left);
                if (node.right != null)
                    queue.add(node.right);
            }
        }

        return height;
    }

    private int getHeightRec(Node<T> node, int height){
        if(node.isLeaf())
            return height;

        int heightLeft = 0;
        int heightRight = 0;

        if(node.left != null)
            heightLeft = getHeightRec(node.left, height + 1);
        if(node.right != null)
            heightRight = getHeightRec(node.right, height + 1);

        return Math.max(heightLeft, heightRight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ParametrizedBinaryTree<?> that))
            return false;
        return getString().equals(that.getString());
    }

    @Override
    public void toFile(String name) throws IOException {
        String path = "/Users/nachopedemonte/Documents/Code/ITBA/EDA/Codigos/Unidad 5/BinaryTree/src/main/resources/" + name;
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
        writer.print(getString());
        writer.close();
    }

    private String getString() {
        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(root);
        StringBuilder sb = new StringBuilder();

        int count = tokenCount;

        while (count != 0) {
            Node<T> current = queue.remove();
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


        public Node() {
        }

        private boolean isLeaf() {
            return left == null && right == null;
        }

        public void preorder() {
            System.out.print(data + " ");

            if (left != null)
                left.preorder();

            if (right != null)
                right.preorder();
        }

        public void postorder() {
            if (left != null)
                left.postorder();

            if (right != null)
                right.postorder();

            System.out.print(data + " ");
        }

        public void printHierarchy(String format) {
            System.out.println(format + "└── " + data);

            if (isLeaf())
                return;

            format += '\t';

            if (left != null)
                left.printHierarchy(format);
            else
                System.out.println(format + "└── null");

            if (right != null)
                right.printHierarchy(format);
            else
                System.out.println(format + "└── null");
        }
    }  // end Node class


	static class NodeHelper<T> {
		
		static enum Action { LEFT, RIGHT, CONSUMIR };
		
		
		private Node<T> aNode;
		private Action anAction;
		
		public NodeHelper(Node<T> aNode, Action anAction ) {
			this.aNode= aNode;
			this.anAction= anAction;
		}
		
		
		public Node<T> getNode() {
			return aNode;
		}
		
		public Action getAction() {
			return anAction;
		}
		
	}


    static class NodeHelper2<T> {

        private final Node<T> aNode;
        private final Function<Node<T>, Node<T>> anAction;

        public NodeHelper2(Node<T> aNode, Function<Node<T>, Node<T>> anAction) {
            this.aNode = aNode;
            this.anAction = anAction;
        }


        public Node<T> getNode() {
            return aNode;
        }

        public Function<Node<T>, Node<T>> getAction() {
            return anAction;
        }

    }

    public static void main(String[] args) throws IOException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ParametrizedBinaryTree<Jefe> rta = new ParametrizedBinaryTree<>("jefe", Jefe.class);
        rta.printHierarchy();

        ParametrizedBinaryTree<Jefe> rta1 = new ParametrizedBinaryTree<>("data1_1", Jefe.class);
        rta1.printHierarchy();

        ParametrizedBinaryTree<Jefe> rta2 = new ParametrizedBinaryTree<>("data0_3", Jefe.class);
        rta2.printHierarchy();
    }
}