package core;

import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BST<T extends Comparable<? super T>> implements BSTreeInterface<T> {
    private Node<T> root;
    private Traversal aTraversal;

    public BST() {
        root = null;
        aTraversal = Traversal.BYLEVELS; // default traversal
    }

    @Override
    public void setTraversal(Traversal traversal) {
        this.aTraversal = traversal;
    }

    @Override
    public Iterator<T> iterator() {
        switch (aTraversal) {
            case BYLEVELS: return new BSTLevelOrderIterator();
            case INORDER: return new BSTInOrderIterator();
            default: throw new RuntimeException("Invalid traversal parameter");
        }
    }

    private class BSTLevelOrderIterator implements Iterator<T> {
        private Queue<Node<T>> queue;

        public BSTLevelOrderIterator() {
            queue = new LinkedList<>();
            if (root != null) {
                queue.offer(root);
            }
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<T> current = queue.poll();
            if (current.getLeft() != null) {
                queue.offer((Node<T>)current.getLeft());
            }
            if (current.getRight() != null) {
                queue.offer((Node<T>)current.getRight());
            }
            return current.getData();
        }
    }

    private class BSTInOrderIterator implements Iterator<T> {
        private Stack<NodeTreeInterface<T>> stack;
        private NodeTreeInterface<T> current;

        public BSTInOrderIterator() {
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

            NodeTreeInterface<T> elementToProcess = stack.pop();
            current = elementToProcess.getRight();
            return elementToProcess.getData();
        }
    }

    @Override
    public void insert(T data) {
        root = insertRec(root, data);
    }

    public void insertDelegated(T data) {
        if (root == null) {
            root = new Node<>(data);
        } else {
            root = root.insert(data);
        }
    }

    private Node<T> insertRec(Node<T> current, T data) {
        if (current == null) {
            return new Node<>(data);
        }

        if (data.compareTo(current.getData()) < 0) {
            current.setLeft(insertRec((Node<T>)current.getLeft(), data));
        } else {
            current.setRight(insertRec((Node<T>)current.getRight(), data));
        }

        return current;
    }

    @Override
    public void preOrder() {
        preOrderRec(root);
    }

    private void preOrderRec(Node<T> node) {
        if (node != null) {
            System.out.print(node.getData() + " ");
            preOrderRec((Node<T>)node.getLeft());
            preOrderRec((Node<T>)node.getRight());
        }
    }

    @Override
    public void postOrder() {
        postOrderRec(root);
    }

    private void postOrderRec(Node<T> node) {
        if (node != null) {
            postOrderRec((Node<T>)node.getLeft());
            postOrderRec((Node<T>)node.getRight());
            System.out.print(node.getData() + " ");
        }
    }

    @Override
    public void inOrder() {
        inOrderRec(root);
    }

    private void inOrderRec(Node<T> node) {
        if (node != null) {
            inOrderRec((Node<T>)node.getLeft());
            System.out.print(node.getData() + " ");
            inOrderRec((Node<T>)node.getRight());
        }
    }

    // version iterativa
    public void inOrderIter() {
        Stack<NodeTreeInterface<T>> stack = new Stack<>();
        
        NodeTreeInterface<T> current = root;
        while (!stack.isEmpty() || current != null) {
            if (current != null) {
                stack.push(current);
                current = current.getLeft();
            } else {
                NodeTreeInterface<T> elementToProcess = stack.pop();
                System.out.print(elementToProcess.getData() + "\t");
                current = elementToProcess.getRight();
            }
        }
    }

    @Override
    public NodeTreeInterface<T> getRoot() {
        return root;
    }

    @Override
    public int getHeight() {
        return getHeightRec(root);
    }

    private int getHeightRec(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getHeightRec((Node<T>)node.getLeft()), 
                           getHeightRec((Node<T>)node.getRight()));
    }

    @Override
    public void remove(T data) {
        if (root != null) {
            root = root.remove(data);
        }
    }

    @Override
    public boolean contains(T myData) {
        return containsRec(root, myData);
    }

    private boolean containsRec(Node<T> current, T myData) {
        if (current == null) {
            return false;
        }
        
        int comparison = myData.compareTo(current.getData());
        if (comparison == 0) {
            return true;
        }
        
        if (comparison < 0) {
            return containsRec((Node<T>)current.getLeft(), myData);
        } else {
            return containsRec((Node<T>)current.getRight(), myData);
        }
    }

    @Override
    public T getMax() {
        if (root == null) {
            return null;
        }
        return getMaxRec(root);
    }

    private T getMaxRec(Node<T> current) {
        if (current.getRight() == null) {
            return current.getData();
        }
        return getMaxRec((Node<T>)current.getRight());
    }

    @Override
    public T getMin() {
        if (root == null) {
            return null;
        }
        return getMinRec(root);
    }

    private T getMinRec(Node<T> current) {
        if (current.getLeft() == null) {
            return current.getData();
        }
        return getMinRec((Node<T>)current.getLeft());
    }

    @Override
    public void printByLevels() {
        if (root == null) {
            return;
        }

        // create an empty queue and enqueue the root node
        Queue<NodeTreeInterface<T>> queue = new LinkedList<>();
        queue.add(root);

        NodeTreeInterface<T> currentNode;

        // hay elementos?
        while (!queue.isEmpty())
        {
            currentNode = queue.remove();
            System.out.print(currentNode.getData() + " ");
            
            if (currentNode.getLeft() != null) {
                queue.add(currentNode.getLeft());
            }

            if (currentNode.getRight() != null) {
                queue.add(currentNode.getRight());
            }
        }

        System.out.println();
    }

    @Override
    public int getOcurrences(T element) {
        if (root == null || element == null) {
            return 0;
        }
        return getOcurrencesRec(root, element);
    }

    private int getOcurrencesRec(Node<T> current, T element) {
        if (current == null) {
            return 0;
        }

        int comparison = element.compareTo(current.getData());
        
        if (comparison == 0) {
            return 1 + getOcurrencesRec((Node<T>)current.getRight(), element);
        } else if (comparison < 0) {
            return getOcurrencesRec((Node<T>)current.getLeft(), element);
        } else { // comparison > 0
            return getOcurrencesRec((Node<T>)current.getRight(), element);
        }
    }

    @Override
    public T Kesimo(int k) {
        if (k <= 0 || root == null) {
            return null;
        }

        Stack<NodeTreeInterface<T>> stack = new Stack<>();
        NodeTreeInterface<T> current = root;
        int count = 0;

        while (current != null || !stack.isEmpty()) {
            // Go as far left as possible
            while (current != null) {
                stack.push(current);
                current = current.getLeft();
            }

            // Process the current node (from stack)
            current = stack.pop();
            count++;
            if (count == k) {
                return current.getData();
            }

            // Move to the right subtree
            current = current.getRight();
        }
        // k is larger than the number of nodes in the tree or other issues
        return null; 
    }

    @Override
    public T getCommonNode(T element1, T element2) {
        if (root == null || element1 == null || element2 == null) {
            return null;
        }
        if (element1.equals(element2)) {
            return null; // Elements must be different
        }

        // Ensure both elements exist in the tree (as per problem: BST does not accept repeats here)
        // The contains method was previously implemented and handles repeats, 
        // but for this specific problem context (no repeats), it will still correctly find unique elements.
        if (!contains(element1) || !contains(element2)) {
            return null;
        }

        Node<T> current = root;
        while (current != null) {
            T currentData = current.getData();
            int compare1 = element1.compareTo(currentData);
            int compare2 = element2.compareTo(currentData);

            if (compare1 < 0 && compare2 < 0) {
                // Both elements are in the left subtree
                current = (Node<T>)current.getLeft();
            } else if (compare1 > 0 && compare2 > 0) {
                // Both elements are in the right subtree
                current = (Node<T>)current.getRight();
            } else {
                // This node is the LCA. 
                // This happens if:
                // 1. One element is in the left subtree and the other is in the right.
                // 2. One of the elements is the current node itself.
                return currentData;
            }
        }
        return null; // Should not be reached if elements are present and different, but as a fallback.
    }

    @Override
    public T getCommonNodeWithRepeated(T element1, T element2) {
        if (root == null || element1 == null || element2 == null) {
            return null;
        }

        // Step 1: Existence checks based on whether elements are equal or distinct
        if (element1.equals(element2)) {
            // If elements are the same, we need at least two occurrences of this value
            if (getOcurrences(element1) < 2) {
                return null;
            }
        } else {
            // If elements are different, both must exist at least once
            if (!contains(element1) || !contains(element2)) {
                return null;
            }
        }

        // Step 2: Find LCA using standard BST LCA logic
        // This logic works because our insert places duplicates to the right,
        // maintaining a structure where the standard LCA search applies.
        Node<T> current = root;
        while (current != null) {
            T currentData = current.getData();

            // Compare element1 and element2 with currentData
            int comp1 = element1.compareTo(currentData);
            int comp2 = element2.compareTo(currentData);

            if (comp1 < 0 && comp2 < 0) {
                // Both elements are strictly smaller than currentData, so LCA must be in the left subtree.
                current = (Node<T>)current.getLeft();
            } else if (comp1 > 0 && comp2 > 0) {
                // Both elements are strictly greater than currentData, so LCA must be in the right subtree.
                current = (Node<T>)current.getRight();
            } else {
                // This node is the LCA. This covers cases where:
                // 1. One element is < currentData and the other is > currentData (split point).
                // 2. One (or both if e1.equals(e2)) element(s) are == currentData.
                //    (If e1 == currentData, and e2 is in a subtree, current is LCA).
                //    (If e1 == currentData, and e2 == currentData, current is LCA).
                return currentData;
            }
        }
        
        return null; // Should ideally not be reached if pre-checks are correct and elements exist.
    }
} 