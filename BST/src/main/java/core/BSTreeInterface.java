package core;

public interface BSTreeInterface<T extends Comparable<? super T>> extends Iterable<T> {

	enum Traversal { BYLEVELS, INORDER }
	
	void setTraversal(Traversal traversal);

	void insert(T data);

	void preOrder();

	void postOrder();

	void inOrder();

	NodeTreeInterface<T> getRoot();
	
	int getHeight();

	void remove(T data);

	boolean contains(T myData);

	T getMax();

	T getMin();

	void printByLevels();

	int getOcurrences(T element);

	T Kesimo(int k);

	T getCommonNode(T element1, T element2);

	T getCommonNodeWithRepeated(T element1, T element2);
}