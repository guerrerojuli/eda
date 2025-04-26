package core;

public class Queue<T> {
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> last;  // Points to the last node
    private int size;

    public Queue() {
        last = null;
        size = 0;
    }

    public void enqueue(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            newNode.next = newNode;  // Points to itself
            last = newNode;
        } else {
            newNode.next = last.next;  // New node points to the first node
            last.next = newNode;       // Last node points to new node
            last = newNode;            // Update last to new node
        }
        size++;
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T data = last.next.data;  // Get data from first node
        if (last.next == last) {  // Only one element
            last = null;
        } else {
            last.next = last.next.next;  // Skip the first node
        }
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return last.next.data;  // Return data from first node
    }

    public boolean isEmpty() {
        return last == null;
    }

    public int size() {
        return size;
    }
}
