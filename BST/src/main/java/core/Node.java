package core;

public class Node<T extends Comparable<? super T>> implements NodeTreeInterface<T> {
    private T data;
    private Node<T> left;
    private Node<T> right;

    public Node(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
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

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    public Node<T> insert(T value) {
        if (value.compareTo(data) < 0) {
            if (left == null) {
                left = new Node<>(value);
            } else {
                left = left.insert(value);
            }
        } else if (value.compareTo(data) > 0) {
            if (right == null) {
                right = new Node<>(value);
            } else {
                right = right.insert(value);
            }
        }
        return this;
    }

    public Node<T> remove(T value) {
        if (value.compareTo(data) < 0) {
            if (left != null) {
                left = left.remove(value);
            }
            return this;
        } 
        if (value.compareTo(data) > 0) {
            if (right != null) {
                right = right.remove(value);
            }
            return this;
        }
        
        // Caso: encontramos el nodo a eliminar (value.compareTo(data) == 0)
        
        // R1: Si es hoja, retornamos null para que el padre actualice su referencia
        if (left == null && right == null) {
            return null;
        }
        
        // R2: Si tiene un solo hijo, retornamos ese hijo para que el padre lo enlace
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        
        // R3: Si tiene dos hijos
        // Buscamos el mayor del subárbol izquierdo (predecesor)
        Node<T> predecessor = left;
        while (predecessor.right != null) {
            predecessor = predecessor.right;
        }
        // Copiamos el dato del predecesor
        this.data = predecessor.getData();
        // Eliminamos el predecesor del subárbol izquierdo
        this.left = left.remove(predecessor.getData());
        
        return this;
    }
} 