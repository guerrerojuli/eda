package core;

public class Node<T extends Comparable<? super T>> implements NodeTreeInterface<T>{

    private T data;
    private Node left;
    private Node right;
    //Para AVL
    private int height;

    public Node(T data) {
        this.data = data;
        this.right = null;
        this.left = null;
    }

    @Override
    public T getData(){
        return data;
    }

    @Override
    public NodeTreeInterface<T> getLeft(){
        return left;
    }

    @Override
    public NodeTreeInterface<T> getRight(){
        return right;
    }

    public void insert(T myData) {
        if(myData.compareTo(data)<=0) {
            if(left == null)
                left = new Node<>(myData);
            else
                left.insert(myData);
        }
        else {
            if(right == null)
                right = new Node<>(myData);
            else
                right.insert(myData);
        }
    }

    public String preorder(StringBuilder s) {
        s.append(data).append(" ");
        if(left!=null)
            left.preorder(s);
        if(right!=null)
            right.preorder(s);
        return s.toString();
    }

    public String postorder(StringBuilder s) {
        if(left!=null)
            left.postorder(s);
        if(right!=null)
            right.postorder(s);
        s.append(data).append(" ");
        return s.toString();
    }

    public String inorder(StringBuilder s) {
        if(left!=null)
            left.inorder(s);
        s.append(data).append(" ");
        if(right!=null)
            right.inorder(s);

        return s.toString();
    }

    public int getHeight() {
        if (left == null && right == null)
            return 0;
        else {
            int leftHeight = (left == null ? 0 : left.getHeight());
            int rightHeight = (right == null ? 0 : right.getHeight());
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    public boolean contains(T myData){
        int c = data.compareTo(myData);
        if(c == 0)
            return true;
        if(left != null && c > 0)
            return left.contains(myData);
        if(right!=null && c < 0)
            return right.contains(myData);
        return false;
    }

    @SuppressWarnings("unchecked")
    public Node<T> delete(T myData){
        int c = myData.compareTo(this.data);
        if(c < 0){
            if(left != null)
                left = left.delete(myData);
            return this;
        }
        if(c > 0){
            if(right != null)
                right = right.delete(myData);
            return this;
        }
        //Si estoy acá, es porque lo encontré.

        //Casos R1 y R2
        if(left == null)
            return right;
        if(right == null)
            return left;

        //Para el caso R3 (ninguno es null, buscamos el predeceor
        //inorder (mas grande de la rama izquierda
        this.data = (T) getPredec(left);
        //Ahora borramos el Predec de donde estaba
        left = left.delete(this.data);
        return this;
    }

    private T getPredec(Node<T> current){
        Node<T> aux = current;
        while(aux.right != null)
            aux = aux.right;
        return aux.data;
    }
}
