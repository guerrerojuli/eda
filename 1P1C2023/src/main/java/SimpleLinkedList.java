public class SimpleLinkedList <T>{
    private Node root = null;
    private Node last = null;

    public void dump() {
        Node current = root;

        while (current!=null ) {
            // avanzo
            System.out.println(current.data);
            current= current.next;
        }
    }

    public void insert(T data) {
        if (root == null && last == null) {
            root = new Node(data, null);
            last = root;
        } else {
            Node newNode = new Node(data, null);
            last.next = newNode;
            last = newNode;
        }
    }

    private final class Node {
        private T data;
        private Node next;

        private Node(T data, Node next) {
            this.data = data;
            this.next = next;
        }
    }
}
