package core;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

abstract public class AdjacencyListGraph<V, E> implements GraphService<V, E> {

	private boolean isSimple;
	protected boolean isDirected;
	private boolean acceptSelfLoop;
	private boolean isWeighted;
	protected String type;
	
	// HashMap no respeta el orden de insercion. En el testing considerar eso
	private Map<V,Collection<InternalEdge>> adjacencyList= new HashMap<>();
	
	// respeta el orden de llegada y facilita el testing
	//	private Map<V,Collection<InternalEdge>> adjacencyList= new LinkedHashMap<>();
	
	protected   Map<V,  Collection<AdjacencyListGraph<V, E>.InternalEdge>> getAdjacencyList() {
		return adjacencyList;
	}
	
	
	protected AdjacencyListGraph(boolean isSimple, boolean isDirected, boolean acceptSelfLoop, boolean isWeighted) {
		this.isSimple = isSimple;
		this.isDirected = isDirected;
		this.acceptSelfLoop= acceptSelfLoop;
		this.isWeighted = isWeighted;

		this.type = String.format("%s %sWeighted %sGraph with %sSelfLoop", 
				isSimple ? "Simple" : "Multi", isWeighted ? "" : "Non-",
				isDirected ? "Di" : "", acceptSelfLoop? "":"No ");
	}
	
	
	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public void addVertex(V aVertex) {

		if (aVertex == null )
		throw new IllegalArgumentException(Messages.getString("addVertexParamCannotBeNull"));
	
		// no edges yet
		getAdjacencyList().putIfAbsent(aVertex, 
				new ArrayList<InternalEdge>());
	}

	
	@Override
	public int numberOfVertices() {
		return getVertices().size();
	}

	@Override
	public Collection<V> getVertices() {
		return getAdjacencyList().keySet() ;
	}

	// Helper method to check if vertex exists
	protected boolean existsVertex(V vertex) {
		return getAdjacencyList().containsKey(vertex);
	}
	
	@Override
	public int numberOfEdges() {
		int totalEdges = 0;
		
		// Count all edges in the adjacency list
		for (V vertex : getAdjacencyList().keySet()) {
			Collection<InternalEdge> edges = getAdjacencyList().get(vertex);
			totalEdges += edges.size();
		}
		
		// If the graph is undirected, we've counted each edge twice
		// (once from each vertex), so we need to divide by 2
		// Exception: self-loops are only counted once even in undirected graphs
		if (!isDirected) {
			int selfLoops = 0;
			
			// Count self-loops separately
			for (V vertex : getAdjacencyList().keySet()) {
				Collection<InternalEdge> edges = getAdjacencyList().get(vertex);
				for (InternalEdge edge : edges) {
					if (edge.target.equals(vertex)) {
						selfLoops++;
					}
				}
			}
			
			// For undirected graphs: (total - selfLoops) / 2 + selfLoops
			totalEdges = (totalEdges - selfLoops) / 2 + selfLoops;
		}
		
		return totalEdges;
	}

	

	@Override
	public void addEdge(V aVertex, V otherVertex, E theEdge) {

		// validation!!!!
		if (aVertex == null || otherVertex == null || theEdge == null)
			throw new IllegalArgumentException(Messages.getString("addEdgeParamCannotBeNull"));

		// es con peso? debe tener implementado el metodo double getWeight()
		if (isWeighted) {
			// reflection
			Class<? extends Object> c = theEdge.getClass();
			try {
				c.getDeclaredMethod("getWeight");
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(
						type + " is weighted but the method double getWeighed() is not declared in theEdge");
			}
		}
		
		if (! acceptSelfLoop && aVertex.equals(otherVertex)) {
			throw new RuntimeException(String.format("%s does not accept self loops between %s and %s" , 
					type, aVertex, otherVertex) );
		}

		// if any of the vertex is not presented, the node is created automatically
		addVertex(aVertex);
		addVertex(otherVertex);
		

	}

	

	
	@Override
	public boolean removeVertex(V aVertex) {
		if (aVertex == null || !getAdjacencyList().containsKey(aVertex)) {
			return false;
		}
		
		// Remove all edges that point to this vertex (incoming edges)
		for (V vertex : getAdjacencyList().keySet()) {
			if (!vertex.equals(aVertex)) { // Skip the vertex we're removing
				Collection<InternalEdge> edges = getAdjacencyList().get(vertex);
				// Use iterator to safely remove while iterating
				edges.removeIf(edge -> edge.target.equals(aVertex));
			}
		}
		
		// Remove the vertex itself (this also removes all its outgoing edges)
		getAdjacencyList().remove(aVertex);
		
		return true;
	}

	@Override
	public boolean removeEdge(V aVertex, V otherVertex) {
		// Validate parameters
		if (aVertex == null || otherVertex == null) {
			return false;
		}
		
		// Check if vertices exist
		if (!getAdjacencyList().containsKey(aVertex) || !getAdjacencyList().containsKey(otherVertex)) {
			return false;
		}
		
		boolean edgeRemoved = false;
		
		// Remove any edge from aVertex to otherVertex
		Collection<InternalEdge> edges = getAdjacencyList().get(aVertex);
		InternalEdge edgeToRemove = null;
		for (InternalEdge edge : edges) {
			if (edge.target.equals(otherVertex)) {
				edgeToRemove = edge;
				break;
			}
		}
		
		if (edgeToRemove != null) {
			edges.remove(edgeToRemove);
			edgeRemoved = true;
		}
		
		// If undirected and not a self-loop, also remove the reverse edge
		if (!isDirected && !aVertex.equals(otherVertex) && edgeRemoved) {
			Collection<InternalEdge> reverseEdges = getAdjacencyList().get(otherVertex);
			InternalEdge reverseEdgeToRemove = null;
			for (InternalEdge edge : reverseEdges) {
				if (edge.target.equals(aVertex)) {
					reverseEdgeToRemove = edge;
					break;
				}
			}
			if (reverseEdgeToRemove != null) {
				reverseEdges.remove(reverseEdgeToRemove);
			}
		}
		
		return edgeRemoved;
	}

	
	@Override
	public boolean removeEdge(V aVertex, V otherVertex, E theEdge) {
		// Validate parameters
		if (aVertex == null || otherVertex == null || theEdge == null) {
			return false;
		}
		
		// Check if vertices exist
		if (!getAdjacencyList().containsKey(aVertex) || !getAdjacencyList().containsKey(otherVertex)) {
			return false;
		}
		
		boolean edgeRemoved = false;
		
		// Remove the specific edge from aVertex to otherVertex
		Collection<InternalEdge> edges = getAdjacencyList().get(aVertex);
		InternalEdge targetEdge = new InternalEdge(theEdge, otherVertex);
		
		if (edges.remove(targetEdge)) {
			edgeRemoved = true;
		}
		
		// If undirected and not a self-loop, also remove the reverse edge
		if (!isDirected && !aVertex.equals(otherVertex) && edgeRemoved) {
			Collection<InternalEdge> reverseEdges = getAdjacencyList().get(otherVertex);
			InternalEdge reverseTargetEdge = new InternalEdge(theEdge, aVertex);
			reverseEdges.remove(reverseTargetEdge);
		}
		
		return edgeRemoved;
	}
	
	
	@Override
	public void dump() {
		// COMPLETAR
		throw new RuntimeException("not implemented yet");
	}
	
	
	@Override
	public int degree(V aVertex) {
		// Only for undirected graphs
		if (isDirected) {
			throw new RuntimeException("degree() method is only applicable to undirected graphs");
		}
		
		if (aVertex == null || !getAdjacencyList().containsKey(aVertex)) {
			throw new RuntimeException("Vertex does not exist");
		}
		
		int degree = 0;
		Collection<InternalEdge> edges = getAdjacencyList().get(aVertex);
		
		for (InternalEdge edge : edges) {
			degree++;
			// Self-loops contribute twice in undirected graphs
			if (edge.target.equals(aVertex)) {
				degree++;
			}
		}
		
		return degree;
	}

	

	@Override
	public int inDegree(V aVertex) {
		// Only for directed graphs
		if (!isDirected) {
			throw new RuntimeException("inDegree() method is only applicable to directed graphs");
		}
		
		if (aVertex == null || !getAdjacencyList().containsKey(aVertex)) {
			throw new RuntimeException("Vertex does not exist");
		}
		
		int inDegree = 0;
		
		// Count edges coming into this vertex from all other vertices
		for (V vertex : getAdjacencyList().keySet()) {
			Collection<InternalEdge> edges = getAdjacencyList().get(vertex);
			for (InternalEdge edge : edges) {
				if (edge.target.equals(aVertex)) {
					inDegree++;
					// Self-loops do NOT contribute twice for inDegree (unlike degree method)
				}
			}
		}
		
		return inDegree;
	}



	@Override
	public int outDegree(V aVertex) {
		// Only for directed graphs
		if (!isDirected) {
			throw new RuntimeException("outDegree() method is only applicable to directed graphs");
		}
		
		if (aVertex == null || !getAdjacencyList().containsKey(aVertex)) {
			throw new RuntimeException("Vertex does not exist");
		}
		
		int outDegree = 0;
		Collection<InternalEdge> edges = getAdjacencyList().get(aVertex);
		
		for (InternalEdge edge : edges) {
			outDegree++;
			// Self-loops do NOT contribute twice for outDegree (unlike degree method)
		}
		
		return outDegree;
	}

	@Override
	public void printBFS(V vertex) {
		if (vertex == null || !getAdjacencyList().containsKey(vertex)) {
			throw new RuntimeException("Vertex does not exist");
		}
		
		Set<V> visited = new HashSet<>();
		Queue<V> queue = new LinkedList<>();
		
		queue.offer(vertex);
		visited.add(vertex);
		
		System.out.print("BFS from " + vertex + ": ");
		while (!queue.isEmpty()) {
			V current = queue.poll();
			System.out.print(current + " ");
			
			// Visit all neighbors
			Collection<InternalEdge> edges = getAdjacencyList().get(current);
			for (InternalEdge edge : edges) {
				V neighbor = edge.target;
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					queue.offer(neighbor);
				}
			}
		}
		System.out.println();
	}

	@Override
	public void printDFS(V vertex) {
		if (vertex == null || !getAdjacencyList().containsKey(vertex)) {
			throw new RuntimeException("Vertex does not exist");
		}
		
		Set<V> visited = new HashSet<>();
		System.out.print("DFS from " + vertex + ": ");
		dfsRecursive(vertex, visited);
		System.out.println();
	}
	
	private void dfsRecursive(V vertex, Set<V> visited) {
		visited.add(vertex);
		System.out.print(vertex + " ");
		
		Collection<InternalEdge> edges = getAdjacencyList().get(vertex);
		for (InternalEdge edge : edges) {
			V neighbor = edge.target;
			if (!visited.contains(neighbor)) {
				dfsRecursive(neighbor, visited);
			}
		}
	}

	@Override
	public Iterable<V> getBFS(V vertex) {
		if (vertex == null || !getAdjacencyList().containsKey(vertex)) {
			throw new RuntimeException("Vertex does not exist");
		}
		
		return new BFSIterable(vertex);
	}

	@Override
	public Iterable<V> getDFS(V vertex) {
		if (vertex == null || !getAdjacencyList().containsKey(vertex)) {
			throw new RuntimeException("Vertex does not exist");
		}
		
		return new DFSIterable(vertex);
	}
	
	@Override
	public abstract void printAllPaths(V start, V end);
	
	@Override
	public abstract DijkstraPath<V,E> dijkstra(V source);

	@Override
	public boolean isBipartite() {
		// Empty graph is bipartite
		if (getAdjacencyList().isEmpty()) {
			return true;
		}
		
		// Map to store colors of vertices (0 = uncolored, 1 = color1, 2 = color2)
		Map<V, Integer> colors = new HashMap<>();
		
		// Initialize all vertices as uncolored
		for (V vertex : getAdjacencyList().keySet()) {
			colors.put(vertex, 0);
		}
		
		// Check each connected component
		for (V startVertex : getAdjacencyList().keySet()) {
			if (colors.get(startVertex) == 0) { // Uncolored vertex
				if (!isBipartiteComponent(startVertex, colors)) {
					return false;
				}
			}
		}
		
		return true;
	}

	// Helper method to check if a connected component is bipartite using BFS
	private boolean isBipartiteComponent(V startVertex, Map<V, Integer> colors) {
		Queue<V> queue = new LinkedList<>();
		queue.offer(startVertex);
		colors.put(startVertex, 1); // Color the start vertex with color 1
		
		while (!queue.isEmpty()) {
			V current = queue.poll();
			int currentColor = colors.get(current);
			int neighborColor = (currentColor == 1) ? 2 : 1; // Alternate color
			
			// Check all adjacent vertices
			Collection<InternalEdge> edges = getAdjacencyList().get(current);
			for (InternalEdge edge : edges) {
				V neighbor = edge.target;
				
				if (colors.get(neighbor) == 0) {
					// Uncolored neighbor - color it with alternate color
					colors.put(neighbor, neighborColor);
					queue.offer(neighbor);
				} else if (colors.get(neighbor) == currentColor) {
					// Neighbor has same color - not bipartite
					return false;
				}
				// If neighbor has different color, continue (this is correct)
			}
		}
		
		return true;
	}

	// Inner class for BFS Iterable
	private class BFSIterable implements Iterable<V> {
		private final V startVertex;
		
		public BFSIterable(V startVertex) {
			this.startVertex = startVertex;
		}
		
		@Override
		public Iterator<V> iterator() {
			return new BFSIterator(startVertex);
		}
	}
	
	// Inner class for BFS Iterator
	private class BFSIterator implements Iterator<V> {
		private final Queue<V> queue;
		private final Set<V> visited;
		
		public BFSIterator(V startVertex) {
			queue = new LinkedList<>();
			visited = new HashSet<>();
			queue.offer(startVertex);
			visited.add(startVertex);
		}
		
		@Override
		public boolean hasNext() {
			return !queue.isEmpty();
		}
		
		@Override
		public V next() {
			if (!hasNext()) {
				throw new java.util.NoSuchElementException();
			}
			
			V current = queue.poll();
			
			// Add neighbors to queue
			Collection<InternalEdge> edges = getAdjacencyList().get(current);
			for (InternalEdge edge : edges) {
				V neighbor = edge.target;
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					queue.offer(neighbor);
				}
			}
			
			return current;
		}
	}
	
	// Inner class for DFS Iterable
	private class DFSIterable implements Iterable<V> {
		private final V startVertex;
		
		public DFSIterable(V startVertex) {
			this.startVertex = startVertex;
		}
		
		@Override
		public Iterator<V> iterator() {
			return new DFSIterator(startVertex);
		}
	}
	
	// Inner class for DFS Iterator (iterative implementation)
	private class DFSIterator implements Iterator<V> {
		private final Stack<V> stack;
		private final Set<V> visited;
		private V nextVertex;
		
		public DFSIterator(V startVertex) {
			stack = new Stack<>();
			visited = new HashSet<>();
			stack.push(startVertex);
			findNext();
		}
		
		private void findNext() {
			nextVertex = null;
			while (!stack.isEmpty() && nextVertex == null) {
				V candidate = stack.pop();
				if (!visited.contains(candidate)) {
					nextVertex = candidate;
					visited.add(candidate);
					
					// Add neighbors to stack (in reverse order to maintain consistent traversal)
					Collection<InternalEdge> edges = getAdjacencyList().get(candidate);
					ArrayList<V> neighbors = new ArrayList<>();
					for (InternalEdge edge : edges) {
						if (!visited.contains(edge.target)) {
							neighbors.add(edge.target);
						}
					}
					// Push in reverse order so that the first neighbor is processed first
					for (int i = neighbors.size() - 1; i >= 0; i--) {
						stack.push(neighbors.get(i));
					}
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			return nextVertex != null;
		}
		
		@Override
		public V next() {
			if (nextVertex == null) {
				throw new java.util.NoSuchElementException();
			}
			
			V current = nextVertex;
			findNext();
			return current;
		}
	}

	@Override
	public boolean hasCycle() {
		// Empty graph has no cycles
		if (getAdjacencyList().isEmpty()) {
			return false;
		}
		
		if (isDirected) {
			return hasDirectedCycle();
		} else {
			return hasUndirectedCycle();
		}
	}

	// Helper method to detect cycles in directed graphs using DFS with recursion stack
	private boolean hasDirectedCycle() {
		// Track visited vertices and recursion stack
		Set<V> visited = new HashSet<>();
		Set<V> recursionStack = new HashSet<>();
		
		// Check each unvisited vertex as a potential starting point
		for (V vertex : getAdjacencyList().keySet()) {
			if (!visited.contains(vertex)) {
				if (hasDirectedCycleDFS(vertex, visited, recursionStack)) {
					return true;
				}
			}
		}
		
		return false;
	}

	// DFS helper for directed cycle detection
	private boolean hasDirectedCycleDFS(V vertex, Set<V> visited, Set<V> recursionStack) {
		visited.add(vertex);
		recursionStack.add(vertex);
		
		// Check all neighbors
		Collection<InternalEdge> edges = getAdjacencyList().get(vertex);
		for (InternalEdge edge : edges) {
			V neighbor = edge.target;
			
			// If neighbor is in recursion stack, we found a back edge (cycle)
			if (recursionStack.contains(neighbor)) {
				return true;
			}
			
			// If neighbor is unvisited, recursively check it
			if (!visited.contains(neighbor)) {
				if (hasDirectedCycleDFS(neighbor, visited, recursionStack)) {
					return true;
				}
			}
		}
		
		// Remove from recursion stack when backtracking
		recursionStack.remove(vertex);
		return false;
	}

	// Helper method to detect cycles in undirected graphs using DFS with parent tracking
	private boolean hasUndirectedCycle() {
		Set<V> visited = new HashSet<>();
		
		// Check each unvisited vertex as a potential starting point
		for (V vertex : getAdjacencyList().keySet()) {
			if (!visited.contains(vertex)) {
				if (hasUndirectedCycleDFS(vertex, visited, null)) {
					return true;
				}
			}
		}
		
		return false;
	}

	// DFS helper for undirected cycle detection
	private boolean hasUndirectedCycleDFS(V vertex, Set<V> visited, V parent) {
		visited.add(vertex);
		
		// Check all neighbors
		Collection<InternalEdge> edges = getAdjacencyList().get(vertex);
		for (InternalEdge edge : edges) {
			V neighbor = edge.target;
			
			// Skip the parent (edge we came from)
			if (neighbor.equals(parent)) {
				continue;
			}
			
			// If neighbor is already visited and it's not the parent, we found a cycle
			if (visited.contains(neighbor)) {
				return true;
			}
			
			// If neighbor is unvisited, recursively check it
			if (hasUndirectedCycleDFS(neighbor, visited, vertex)) {
				return true;
			}
		}
		
		return false;
	}

	

	
	
	class InternalEdge {
		E edge;
		V target;

		InternalEdge(E propEdge, V target) {
			this.target = target;
			this.edge = propEdge;
		}

		@Override
		public boolean equals(Object obj) {
			@SuppressWarnings("unchecked")
			InternalEdge aux = (InternalEdge) obj;

			return ((edge == null && aux.edge == null) || (edge != null && edge.equals(aux.edge)))
					&& target.equals(aux.target);
		}

		@Override
		public int hashCode() {
			return target.hashCode();
		}

		@Override
		public String toString() {
			return String.format("-[%s]-(%s)", edge, target);
		}
	}
	
	// Getters for subclasses
	protected boolean isWeighted() {
		return isWeighted;
	}

	protected boolean isSimple() {
		return isSimple;
	}

	protected boolean acceptsSelfLoop() {
		return acceptSelfLoop;
	}
}
