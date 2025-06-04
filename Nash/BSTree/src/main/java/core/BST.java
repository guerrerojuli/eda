package core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BST<T extends Comparable<? super T>> implements BSTreeInterface<T> {

    private Node<T> root = null;
    private Traversal traversal = Traversal.BYLEVELS;

    @Override
    public void setTraversal(Traversal traversal){
        this.traversal = traversal;
    }

    @Override
    public void insert(T myData) {
        if(root == null)
            root = new Node<>(myData);
        else
            root.insert(myData);
    }

    @Override
    public void preOrder() {
        if(root==null)
            throw new IllegalStateException();
        System.out.println(root.preorder(new StringBuilder()));
    }

    @Override
    public void postOrder() {
        if(root==null)
            throw new IllegalStateException();
        System.out.println(root.postorder(new StringBuilder()));
    }

    @Override
    public void inOrder() {
        if(root==null)
            throw new IllegalStateException();
        System.out.println(root.inorder(new StringBuilder()));
    }

    @Override
    public NodeTreeInterface<T> getRoot() {
        return root;
    }

    @Override
    public int getHeight() {
        return root.getHeight();
    }

    @Override
    public boolean contains(T myData){
        if(root == null)
            return false;
        return root.contains(myData);
    }

    @Override
    public T getMax(){
        NodeTreeInterface<T> current = root;
        while(current != null){
            if (current.getRight() == null)
                return current.getData();
            current = current.getRight();
        }
        return null;
    }

    @Override
    public T getMin(){
        NodeTreeInterface<T> current = root;
        while(current != null){
            if (current.getLeft() == null)
                return current.getData();
            current = current.getLeft();
        }
        return null;
    }

    @Override
    public void delete(T myData){
        if(myData == null)
            throw new IllegalArgumentException("Data cannot be null");
        if(root != null)
            root = root.delete(myData);
    }

    @Override
    public void printByLevels(){
        if (root == null)
            return;
        // create an empty queue and enqueue the root node
        Queue<NodeTreeInterface<T>> queue = new LinkedList<>();
        queue.add (root);
        NodeTreeInterface<T> currentNode;
        // hay elementos?
        while (!queue.isEmpty()) {
            currentNode = queue.remove();
            System.out.print(currentNode.getData() + " ");

            if (currentNode.getLeft() != null)
                queue.add(currentNode.getLeft());
            if (currentNode.getRight() != null)
                queue.add(currentNode.getRight());
        }
        System.out.println();
    }

    @Override
    public Iterator<T> iterator(){
        return switch (traversal){
            case BYLEVELS -> new LevelIterator();
            case INORDER -> new InOrderIterator();
        };
    }

    private class LevelIterator implements Iterator<T>{

        private final Queue<NodeTreeInterface<T>> queue;

        private LevelIterator() {
            queue = new LinkedList<>();

            if (root != null)
                queue.add(root);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public T next() {
            NodeTreeInterface<T> currentNode = queue.remove();

            if (currentNode.getLeft() != null)
                queue.add(currentNode.getLeft());

            if (currentNode.getRight() != null)
                queue.add(currentNode.getRight());

            return currentNode.getData();
        }

    }

    private class InOrderIterator implements Iterator<T>{

        private final Stack<NodeTreeInterface<T>> stack;
        NodeTreeInterface<T> current;

        private InOrderIterator() {
            stack = new Stack<>();
            current = root;
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty() || current != null;
        }

        @Override
        public T next() {
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }
            NodeTreeInterface<T> nodeToProcess = stack.pop();
            current = nodeToProcess.getRight();
            return nodeToProcess.getData();
        }
    }

    public static void main(String[] args) {
        BST<Integer> myTree = new BST<>();
        myTree.insert(50);
        myTree.insert(60);
        myTree.insert(80);
        myTree.insert(20);
        myTree.insert(70);
        myTree.insert(40);
        myTree.insert(44);
        myTree.insert(10);
        myTree.insert(40);

        myTree.printByLevels();

        myTree.delete(80);
        myTree.delete(10);

        myTree.printByLevels();

        for (Integer data : myTree) {
            System.out.print(data + " ");
        }
        System.out.print('\n');
        myTree.forEach( t-> System.out.print(t + " ") );
        System.out.println('\n');

        myTree.setTraversal(Traversal.INORDER);

        for (Integer data : myTree) {
            System.out.print(data + " ");
        }
        System.out.print('\n');
    }
}
