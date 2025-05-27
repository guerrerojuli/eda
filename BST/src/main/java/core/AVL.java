package core;

import java.util.Iterator;
import java.util.NoSuchElementException; // For iterators
import java.util.LinkedList; // For printByLevels and iterators
import java.util.Queue; // For printByLevels and iterators
import java.util.Stack; // For iterators or iterative traversals

public class AVL<T extends Comparable<? super T>> implements BSTreeInterface<T> {

    private AVLNode<T> root;
    private Traversal aTraversal; // For the iterator

    private static class AVLNode<T extends Comparable<? super T>> implements NodeTreeInterface<T> {
        T data;
        AVLNode<T> left;
        AVLNode<T> right;
        int height;

        AVLNode(T data) {
            this.data = data;
            this.height = 0; // Height of a new leaf node is 0 (consistent with image's logic)
        }

        @Override
        public T getData() {
            return data;
        }

        @Override
        public NodeTreeInterface<T> getLeft() {
            return left;
        }

        @Override
        public NodeTreeInterface<T> getRight() {
            return right;
        }

        // AVLNode specific methods, not in NodeTreeInterface
        public void setLeft(AVLNode<T> left) {
            this.left = left;
        }

        public void setRight(AVLNode<T> right) {
            this.right = right;
        }
    }

    public AVL() {
        root = null;
        aTraversal = Traversal.INORDER; // Default traversal for AVL
    }

    // Helper methods for AVL
    private int height(AVLNode<T> node) {
        return (node == null) ? -1 : node.height; // Null nodes have height -1
    }

    private void updateHeight(AVLNode<T> node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    private int getBalanceFactor(AVLNode<T> node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // Rotation methods
    private AVLNode<T> rotateRight(AVLNode<T> y) {
        AVLNode<T> x = y.left;
        AVLNode<T> T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        updateHeight(y);
        updateHeight(x);

        return x; // New root of this subtree
    }

    private AVLNode<T> rotateLeft(AVLNode<T> x) {
        AVLNode<T> y = x.right;
        AVLNode<T> T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        return y; // New root of this subtree
    }

    // BSTreeInterface methods implementation

    @Override
    public void insert(T data) {
        if (data == null) {
            // Or throw IllegalArgumentException, depending on desired behavior
            return; 
        }
        root = insertRec(root, data);
    }

    private AVLNode<T> insertRec(AVLNode<T> node, T data) {
        // 1. Standard BST insertion
        if (node == null) {
            return new AVLNode<>(data);
        }

        int compareResult = data.compareTo(node.data);

        if (compareResult < 0) {
            node.left = insertRec(node.left, data);
        } else if (compareResult > 0) {
            node.right = insertRec(node.right, data);
        } else {
            // Duplicate data not allowed in this AVL example as per typical AVL,
            // or handle as per specific requirements if duplicates are allowed.
            // For now, if duplicates are found, we just return the node without changes.
            return node; 
        }

        // 2. Update height of this ancestor node (using image's logic)
        int leftChildEffectiveHeight = (node.left == null) ? -1 : node.left.height;
        int rightChildEffectiveHeight = (node.right == null) ? -1 : node.right.height;
        node.height = 1 + Math.max(leftChildEffectiveHeight, rightChildEffectiveHeight);

        // 3. Get the balance factor for rotation decisions (using image's implied logic)
        int balance = leftChildEffectiveHeight - rightChildEffectiveHeight;

        // 4. If node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && data.compareTo(node.left.data) < 0) {
            return rotateRight(node);
        }

        // Right Right Case
        if (balance < -1 && data.compareTo(node.right.data) > 0) {
            return rotateLeft(node);
        }

        // Left Right Case
        if (balance > 1 && data.compareTo(node.left.data) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Left Case
        if (balance < -1 && data.compareTo(node.right.data) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        // Return the (unchanged) node pointer if no rotation needed
        return node;
    }

    @Override
    public boolean contains(T myData) {
        if (myData == null) return false;
        AVLNode<T> current = root;
        while (current != null) {
            int compareResult = myData.compareTo(current.data);
            if (compareResult < 0) {
                current = current.left;
            } else if (compareResult > 0) {
                current = current.right;
            } else {
                return true; // Element found
            }
        }
        return false; // Element not found
    }
    
    @Override
    public void setTraversal(Traversal traversal) {
        this.aTraversal = traversal;
    }

    // Basic InOrder traversal for printing
    @Override
    public void inOrder() {
        System.out.print("InOrder: ");
        inOrderRec(root);
        System.out.println();
    }

    private void inOrderRec(AVLNode<T> node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.data + " ");
            inOrderRec(node.right);
        }
    }

    @Override
    public void preOrder() {
        System.out.print("PreOrder: ");
        preOrderRec(root);
        System.out.println();
    }

    private void preOrderRec(AVLNode<T> node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preOrderRec(node.left);
            preOrderRec(node.right);
        }
    }

    @Override
    public void postOrder() {
        System.out.print("PostOrder: ");
        postOrderRec(root);
        System.out.println();
    }

    private void postOrderRec(AVLNode<T> node) {
        if (node != null) {
            postOrderRec(node.left);
            postOrderRec(node.right);
            System.out.print(node.data + " ");
        }
    }
    
    @Override
    public void printByLevels() {
        if (root == null) {
            System.out.println("Tree is empty.");
            return;
        }
        Queue<AVLNode<T>> queue = new LinkedList<>();
        queue.offer(root);
        int level = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            System.out.print("Level " + level + ": ");
            for (int i = 0; i < levelSize; i++) {
                AVLNode<T> current = queue.poll();
                System.out.print(current.data + "(BF:" + getBalanceFactor(current) + ") ");
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            System.out.println();
            level++;
        }
    }


    @Override
    public NodeTreeInterface<T> getRoot() {
        return root; // AVLNode<T> implements NodeTreeInterface<T>
    }

    @Override
    public int getHeight() {
        return height(root);
    }

    @Override
    public T getMin() {
        if (root == null) return null;
        AVLNode<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.data;
    }

    @Override
    public T getMax() {
        if (root == null) return null;
        AVLNode<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.data;
    }
    
    // Iterator implementation (simple InOrder iterator for now)
    @Override
    public Iterator<T> iterator() {
        // For simplicity, only implementing InOrder iterator here.
        // BYLEVELS could be implemented similarly to BSTLevelOrderIterator if needed.
        if (aTraversal == Traversal.INORDER) {
            return new AVLInOrderIterator();
        } else if (aTraversal == Traversal.BYLEVELS) {
             return new AVLLevelOrderIterator();
        }
        throw new UnsupportedOperationException("Traversal type not supported yet for AVL iterator.");
    }

    private class AVLInOrderIterator implements Iterator<T> {
        private Stack<AVLNode<T>> stack;
        private AVLNode<T> current;

        public AVLInOrderIterator() {
            stack = new Stack<>();
            this.current = AVL.this.root;
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty() || current != null;
        }

        @Override
        public T next() {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            if (stack.isEmpty()) {
                throw new NoSuchElementException();
            }
            AVLNode<T> node = stack.pop();
            current = node.right;
            return node.data;
        }
    }
    
    private class AVLLevelOrderIterator implements Iterator<T> {
        private Queue<AVLNode<T>> queue;

        public AVLLevelOrderIterator() {
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
            AVLNode<T> current = queue.poll();
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
            return current.data;
        }
    }

    // Stubbed methods (complex or not core to immediate request)
    @Override
    public void remove(T data) {
        // AVL removal is complex, involves rebalancing.
        // Not implementing fully for this step.
        // root = removeRec(root, data); // If to be implemented
        throw new UnsupportedOperationException("Remove operation is not implemented yet for AVL.");
    }
    
    // Recursive remove helper (if to be implemented)
    /*
    private AVLNode<T> removeRec(AVLNode<T> node, T data) {
        if (node == null) {
            return null;
        }

        int compareResult = data.compareTo(node.data);

        if (compareResult < 0) {
            node.left = removeRec(node.left, data);
        } else if (compareResult > 0) {
            node.right = removeRec(node.right, data);
        } else { // Node to be deleted found
            // Case 1: Node with only one child or no child
            if (node.left == null || node.right == null) {
                AVLNode<T> temp = (node.left != null) ? node.left : node.right;
                if (temp == null) { // No child case
                    node = null;
                } else { // One child case
                    node = temp; // Copy contents of the non-empty child
                }
            } else { // Case 2: Node with two children
                AVLNode<T> temp = minValueNode(node.right); // Get inorder successor
                node.data = temp.data; // Copy the inorder successor's data to this node
                node.right = removeRec(node.right, temp.data); // Delete the inorder successor
            }
        }

        if (node == null) { // If the tree had only one node then return
            return null;
        }

        // Update height
        updateHeight(node);

        // Get balance factor
        int balance = getBalanceFactor(node);

        // Rebalance (similar to insert)
        // Left Left Case
        if (balance > 1 && getBalanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }
        // Left Right Case
        if (balance > 1 && getBalanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        // Right Right Case
        if (balance < -1 && getBalanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }
        // Right Left Case
        if (balance < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private AVLNode<T> minValueNode(AVLNode<T> node) {
        AVLNode<T> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }
    */


    @Override
    public int getOcurrences(T element) {
        // AVL trees typically do not store duplicates. 
        // If they were to, this method would need careful thought on how duplicates are handled by insert.
        // Assuming no duplicates for standard AVL, an element occurs 0 or 1 time.
        return contains(element) ? 1 : 0; 
    }

    @Override
    public T Kesimo(int k) {
        // This can be implemented with an in-order traversal approach, similar to BST.
        // The AVL property doesn't change the logic for finding k-th smallest.
        if (k <= 0 || root == null) {
            return null;
        }
        Stack<AVLNode<T>> stack = new Stack<>();
        AVLNode<T> current = root;
        int count = 0;
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            current = stack.pop();
            count++;
            if (count == k) {
                return current.data;
            }
            current = current.right;
        }
        return null; // k is out of bounds
    }

    @Override
    public T getCommonNode(T element1, T element2) {
        // Standard LCA algorithm for BST applies to AVL as well.
        // Assuming elements are distinct and present (as per original problem context for this method).
        if (root == null || element1 == null || element2 == null || element1.equals(element2)) {
            return null;
        }
        // Basic contains check for AVL; assumes no duplicates for this specific method's contract.
        if (!contains(element1) || !contains(element2)) {
             return null;
        }

        AVLNode<T> current = root;
        while (current != null) {
            T currentData = current.data;
            int compare1 = element1.compareTo(currentData);
            int compare2 = element2.compareTo(currentData);

            if (compare1 < 0 && compare2 < 0) {
                current = current.left;
            } else if (compare1 > 0 && compare2 > 0) {
                current = current.right;
            } else {
                return currentData; // LCA found
            }
        }
        return null; // Should not be reached if elements are in the tree
    }

    @Override
    public T getCommonNodeWithRepeated(T element1, T element2) {
        // The current AVL insert does not handle duplicates. 
        // If it did, and duplicates were allowed, the logic would be similar to getCommonNode,
        // but existence checks (getOcurrences) would be based on the AVL's duplicate handling.
        // For now, as insertRec doesn't insert duplicates, this will behave like getCommonNode.
        // If insert were to handle duplicates (e.g., by incrementing a count or storing a list),
        // this method would need significant rework.
        // For now, will treat as if no duplicates based on current insert.
        if (root == null || element1 == null || element2 == null) {
            return null;
        }
        
        boolean e1Exists = contains(element1);
        boolean e2Exists = contains(element2);

        if (element1.equals(element2)) {
            // If AVL allows duplicates (e.g. by counting), this would use getOcurrences.
            // Since our AVL insert currently doesn't allow duplicates, an element can appear at most once.
            // So, for element1 == element2, common node doesn't make sense unless we redefine.
            // Sticking to "at least 2 occurrences of said value". Current AVL has 0 or 1.
            return null; // Cannot have 2 occurrences of the same value with current insert.
        } else {
            if (!e1Exists || !e2Exists) {
                return null;
            }
        }
        
        // Proceed with LCA finding, identical to getCommonNode as duplicates aren't currently stored.
        AVLNode<T> current = root;
        while (current != null) {
            T currentData = current.data;
            int compare1 = element1.compareTo(currentData);
            int compare2 = element2.compareTo(currentData);

            if (compare1 < 0 && compare2 < 0) {
                current = current.left;
            } else if (compare1 > 0 && compare2 > 0) {
                current = current.right;
            } else {
                return currentData;
            }
        }
        return null;
    }
} 