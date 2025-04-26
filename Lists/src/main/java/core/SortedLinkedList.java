package core;

import java.util.Iterator;
import java.util.NoSuchElementException;

// lista simplemente encadenada, no acepta repetidos (false e ignora) ni nulls (exception)
public class SortedLinkedList<T extends Comparable<? super T>> implements SortedListService<T>{

	private Node root;
	private Node last;
	private int size;
	
	@Override
	public boolean insert(T data) {
		return insert1(data);  // Using the iterative implementation as default
	}
	
	// insert resuelto todo en la clase SortedLinkedList, iterativo
	public boolean insert1(T data) {
		
		if (data == null) 
			throw new IllegalArgumentException("data cannot be null");

		Node prev= null;
		Node current = root;

		while (current!=null && current.data.compareTo(data) < 0) {
			// avanzo
			prev= current;
			current= current.next;
		}

		// repetido?
		if (current!=null && current.data.compareTo(data) == 0) {
			System.err.println(String.format("Insertion failed. %s repeated", data));
			return false;
		}

		Node aux= new Node(data, current);
		// es el lugar para colocarlo
		if (current == root) {
			// el primero es un caso especial: cambia root
			root= aux;
		}
		else {
			// nodo interno
			prev.next= aux;
		}

		// actualizar last si es necesario
		if (current == null) {
			last = aux;
		}
		
		size++;
		return true;
	}

	
	// insert resuelto todo en la clase SortedLinkedList, recursivo
	public boolean insert2(T data) {
		if (data == null) 
			throw new IllegalArgumentException("data cannot be null");
		
		boolean[] rta = new boolean[1];
		root= insertRec(data, root, rta);
		return rta[0];
	}
	
	
	public Node insertRec(T data, Node current, boolean[] rta) {
		if (current == null) {
			rta[0] = true;
			return new Node(data, null);
		}
		int cmp = current.data.compareTo(data);
		if (cmp == 0) {
			System.err.println(String.format("Insertion failed. %s repeated", data));
			rta[0] = false;
			return current;
		}
		if (cmp < 0) {
			current.next = insertRec(data, current.next, rta);
			return current;
		}
		rta[0] = true;
		return new Node(data, current);

	}
	
	// insert resuelto delegando al nodo
	public boolean insert3(T data) {
		if (data == null) 
			throw new IllegalArgumentException("data cannot be null");

		root = root.insert(data);
		return true;
	}
	
	

	
	
	@Override
	public boolean find(T data) {
		return getPos(data) != -1;
	}
	
	
	// delete resuelto todo en la clase SortedLinkedList, iterativo
	@Override
	public boolean remove(T data) {
		if (data == null) 
			throw new IllegalArgumentException("data cannot be null");

		Node prev = null;
		Node current = root;

		while (current != null && current.data.compareTo(data) < 0) {
			prev = current;
			current = current.next;
		}

		if (current == null || current.data.compareTo(data) != 0) {
			return false;  // Element not found
		}

		// Element found, remove it
		if (prev == null) {
			// Removing first element
			root = current.next;
		} else {
			// Removing internal element
			prev.next = current.next;
		}

		// actualizar last si es necesario
		if (current.next == null) {
			last = prev;
		}

		size--;
		return true;
	}
	
	
	// delete resuelto todo en la clase SortedLinkedList, recursivo
//	@Override
	public boolean remove2(T data) {
		if (data == null) 
			throw new IllegalArgumentException("data cannot be null");

		boolean[] rta = new boolean[1];
		root = removeRec(data, root, rta);
		return rta[0];

	}
	

	public Node removeRec(T data, Node current, boolean[] rta) {
		if (current == null) {
			rta[0] = false;
			return null;
		}
		int cmp = current.data.compareTo(data);
		if (cmp == 0) {
			rta[0] = true;
			return current.next;
		}
		if (cmp < 0) {
			current.next = removeRec(data, current.next, rta);
			return current;
		}
		rta[0] = false;
		return current;
	}


	// delete resuelto delegando al nodo
//	@Override
	public boolean remove3(T data) {
		if (data == null) 
			throw new IllegalArgumentException("data cannot be null");

		root = root.remove(data);
		return true;
	}
	
	
	
	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public int size() {
		return size;
	}

	
	@Override
	public void dump() {
		Node current = root;

		while (current!=null ) {
			// avanzo
			System.out.println(current.data);
			current= current.next;
		}
	}
	
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !  (other instanceof SortedLinkedList) )
			return false;
		
		@SuppressWarnings("unchecked")
		SortedLinkedList<T> auxi = (SortedLinkedList<T>) other;
		
		// Check if sizes are different
		if (size != auxi.size)
			return false;
		
		Node current = root;
		Node currentOther= auxi.root;
		while (current!=null && currentOther != null ) {
			// Check if elements are different
			if (current.data.compareTo(currentOther.data) != 0)
				return false;
			
			// Move to next elements
			current= current.next;
			currentOther= currentOther.next;
		}
		
		// Both lists should be exhausted at the same time
		return current == null && currentOther == null;
	}
	
	// -1 si no lo encontro
	protected int getPos(T data) {
		Node current = root;
		int pos= 0;
		
		while (current!=null ) {
			if (current.data.compareTo(data) == 0)
				return pos;
			
			// avanzo
			current= current.next;
			pos++;
		}
		return -1;
	}
	
	@Override
	public T getMin() {
		if (root == null)
			return null;
		
		return root.data;
	}


	@Override
	public T getMax() {
		if (last == null)
			return null;
		
		return last.data;
	}


	
	
	private final class Node {
		private T data;
		private Node next;
	
		private Node(T data, Node next) {
			this.data= data;
			this.next= next;
		}

		public Node insert(T data) {
			return insertRec(data, this, new boolean[1]);
		}

		public Node remove(T data) {
			return removeRec(data, this, new boolean[1]);
		}
	}
	

	

	
	public static void main(String[] args) {
		SortedLinkedList<String> l = new SortedLinkedList<>();
	
		System.out.println("lista " +  (l.isEmpty()? "":"NO") + " vacia");
		System.out.println(l.size() );
		System.out.println(l.getMin() );
		System.out.println(l.getMax() );
		System.out.println();
		
		System.out.println(l.insert("hola"));
		l.dump();
		System.out.println();
		
		System.out.println("lista " +  (l.isEmpty()? "":"NO") + " vacia");
		System.out.println();
		
		System.out.println(l.insert("tal"));
		l.dump();
		System.out.println();
		
		System.out.println(l.insert("ah"));
		l.dump();
		System.out.println();
		
		System.out.println(l.insert("veo"));
		l.dump();
		System.out.println();
		
		System.out.println(l.insert("bio"));
		l.dump();
		System.out.println();
		
		System.out.println(l.insert("tito"));
		l.dump();
		System.out.println();


		System.out.println(l.insert("hola"));
		l.dump();
		System.out.println();
		
		
		System.out.println(l.insert("aca"));
		l.dump();
		System.out.println();
		
		System.out.println(l.size() );
		System.out.println(l.getMin() );
		System.out.println(l.getMax() );
	}

	@Override
	public Iterator<T> iterator() {
		return new SortedLinkedListIterator();
	}

	private class SortedLinkedListIterator implements Iterator<T> {
		private Node current;
		private Node previous;
		private Node previousPrevious; // Para mantener el nodo anterior al anterior

		public SortedLinkedListIterator() {
			current = root;
			previous = null;
			previousPrevious = null;
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T data = current.data;
			previousPrevious = previous;
			previous = current;
			current = current.next;
			return data;
		}

		@Override
		public void remove() {
			if (previous == null) {
				throw new IllegalStateException("No element to remove");
			}

			// Actualizar la lista
			if (previousPrevious == null) {
				// Estamos eliminando el primer elemento
				root = current;
			} else {
				previousPrevious.next = current;
			}

			// Actualizar last si es necesario
			if (current == null) {
				last = previousPrevious;
			}

			// Actualizar el tamaño
			size--;

			// Resetear el estado para la próxima operación
			previous = previousPrevious;
		}
	}
}
