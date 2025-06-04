package core;
 
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.ArrayList;
import java.lang.reflect.Method;



public class SimpleOrDefault<V,E> extends AdjacencyListGraph<V,E> {

	protected SimpleOrDefault(boolean isDirected, boolean acceptSelfLoops, boolean isWeighted) {
		super(true, isDirected, acceptSelfLoops, isWeighted);
	
	}
	
	
	@Override
	public void addEdge(V aVertex, V otherVertex, E theEdge) {

		// validacion y creacion de vertices si fuera necesario
		super.addEdge(aVertex, otherVertex, theEdge);

		// Check if edge already exists (simple graph constraint)
		Collection<InternalEdge> adjacentEdges = getAdjacencyList().get(aVertex);
		for (InternalEdge existingEdge : adjacentEdges) {
			if (existingEdge.target.equals(otherVertex)) {
				throw new RuntimeException(String.format(
					"Simple graph does not allow multiple edges between %s and %s", 
					aVertex, otherVertex));
			}
		}
		
		// Add the edge
		InternalEdge newEdge = new InternalEdge(theEdge, otherVertex);
		adjacentEdges.add(newEdge);
		
		// If undirected and not a self-loop, add reverse edge
		if (!isDirected && !aVertex.equals(otherVertex)) {
			Collection<InternalEdge> reverseAdjacentEdges = getAdjacencyList().get(otherVertex);
			InternalEdge reverseEdge = new InternalEdge(theEdge, aVertex);
			reverseAdjacentEdges.add(reverseEdge);
		}
	}

	@Override
	// Dijkstra exige que los pesos sean positivos!!!
	public DijkstraPath<V, E> dijkstra(V source) {
		// Validate that the graph is weighted
		if (!isWeighted()) {
			throw new RuntimeException("Dijkstra algorithm requires a weighted graph");
		}
		
		// Validate source vertex
		if (source == null || !existsVertex(source)) {
			throw new RuntimeException("Source vertex does not exist");
		}

		
		PriorityQueue<NodePQ> pq= new PriorityQueue<>();
	
		//stores shortest distance from source to every vertex
		Map<V,Integer> costo = new HashMap<>();
		Map<V,V> prev= new HashMap<>();
	
		// empieza vacio
		Set<V> nodesVisisted= new HashSet<>();
	
		// inicializacion+
		for(V aV: getAdjacencyList().keySet() ) {
			if (aV.equals(source)) {
			pq.add(new NodePQ(source, 0));
			costo.put(source, 0);
			}
			else {
			costo.put(aV, Integer.MAX_VALUE);
			}
		prev.put(aV, null);
		}
		
		while( ! pq.isEmpty()) {
			NodePQ current = pq.poll(); // el menor
			
			if (nodesVisisted.contains(current.vertex)) // ya lo procese
				continue;
	
			// a procesarlo! Con esto tambien se ignora self-loop
			nodesVisisted.add(current.vertex);
	
			// ahora recorrer todos los ejes incidentes a current
			Collection<AdjacencyListGraph<V, E>.InternalEdge> adjList = getAdjacencyList().get(current.vertex);
			for(InternalEdge neighbor: adjList) {
				// si fue visitado seguir. Esto tambien excluye los self loops...
				if ( nodesVisisted.contains(neighbor.target)) {
					continue;
				}
	
				// invocando a getWeight (se ha validado en insercion)
				int weight=0;
				// peso de ese eje?
				try {
					Method fn = neighbor.edge.getClass().getMethod("getWeight");
					weight = (int) fn.invoke(neighbor.edge);
				}
				catch (Exception e) {
				throw new RuntimeException(e);
				}

				// verificacion
				if (weight < 0 )
					throw new IllegalArgumentException(
							String.format(Messages.getString("dijkstraWithNegativeWeight"),
										current.vertex, neighbor.target, weight));
	
				// cual seria el costo de neighbor viniendo desde current?
				int newCosto = costo.get(current.vertex) + weight;

				// es una mejora?
				if (newCosto < costo.get(neighbor.target) ) {
					// insertar neighbor con ese valor mejorado
					costo.put(neighbor.target, newCosto);
					pq.add(new NodePQ(neighbor.target, newCosto));
					
					// armar camino
					prev.put(neighbor.target, current.vertex);
				}
			}
		}
		return new DijkstraPath<>(costo, prev);
	}

	@Override
	public void printAllPaths(V startNode, V endNode) {
		// Validate parameters
		if (startNode == null || endNode == null) {
			throw new RuntimeException("Start and end vertices cannot be null");
		}
		
		if (!existsVertex(startNode)) {
			throw new RuntimeException("Start vertex does not exist");
		}
		
		if (!existsVertex(endNode)) {
			throw new RuntimeException("End vertex does not exist");
		}

		if (acceptsSelfLoop())
			throw new RuntimeException(Messages.getString("printAllPathsNotDefinedForGraphsWithSelfLoops"));
		
		Set<V> visited= new HashSet<V>();
		ArrayList<V> path= new ArrayList<>();
		ArrayList<ArrayList<V>> allPaths = new ArrayList<>();
		
		// Collect all paths
		collectAllPaths(startNode, endNode, visited, path, allPaths);
		
		// Print results in the expected format
		if (allPaths.isEmpty()) {
			System.out.println("No paths found from " + startNode + " to " + endNode);
		} else {
			System.out.println("All paths from " + startNode + " to " + endNode + ":");
			for (int i = 0; i < allPaths.size(); i++) {
				ArrayList<V> currentPath = allPaths.get(i);
				System.out.print("Path " + (i + 1) + ": ");
				for (int j = 0; j < currentPath.size(); j++) {
					System.out.print(currentPath.get(j));
					if (j < currentPath.size() - 1) {
						System.out.print(" -> ");
					}
				}
				System.out.println();
			}
			System.out.println("Total paths found: " + allPaths.size());
		}
	}
	
	
	private void collectAllPaths(V startNode, V endNode, Set<V> visited, ArrayList<V> path, ArrayList<ArrayList<V>> allPaths) {
		// proceso el nodo
		path.add(startNode);
		visited.add(startNode);
		
		// end?
		if (startNode.equals(endNode)) {
			// Add a copy of the current path to allPaths
			allPaths.add(new ArrayList<>(path));
			
			// deshago aca porque no voy a pasar por flujo normal
			visited.remove(endNode);
			path.remove(path.size() - 1);
			return;
		}
		
		// go on
		Collection<InternalEdge> adjListOther = getAdjacencyList().get(startNode);
		for (InternalEdge internalEdge : adjListOther) {
			if (! visited.contains(internalEdge.target)) {
				collectAllPaths(internalEdge.target, endNode, visited, path, allPaths);
			}
		}
		
		// lo deshago
		visited.remove(startNode);
		path.remove(path.size() - 1);
	}

	// Clase interna para la PriorityQueue
	class NodePQ implements Comparable<NodePQ> {
		V vertex;
		Double distance;

		public NodePQ(V vertex, double distance) {
			this.vertex= vertex;
			this.distance= distance;
		}
		
		@Override
		public int compareTo(NodePQ o2) {
			return Double.compare( distance, o2.distance);
		}
	}


}
