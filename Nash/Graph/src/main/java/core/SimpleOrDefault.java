package core;
 
import java.lang.reflect.Method;
import java.util.*;

public class SimpleOrDefault<V,E> extends AdjacencyListGraph<V,E> {

	protected SimpleOrDefault(boolean isDirected, boolean acceptSelfLoops, boolean isWeighted) {
		super(true, isDirected, acceptSelfLoops, isWeighted);
	}

	@Override
	public void addEdge(V aVertex, V otherVertex, E theEdge) {

		// validacion y creacion de vertices si fuera necesario
		super.addEdge(aVertex, otherVertex, theEdge);

		Collection<InternalEdge> adjListSrc = getAdjacencyList().get(aVertex);

		// if exists edge with same target...
		for (InternalEdge internalEdge : adjListSrc) {
			if (internalEdge.target.equals(otherVertex))
				throw new IllegalArgumentException(
						String.format(Messages.getString("addEdgeSimpleOrDefaultError"), aVertex, otherVertex) ); //$NON-NLS-1$
		}

		// creacion de ejes
		adjListSrc.add(new InternalEdge(theEdge, otherVertex));

		if (!isDirected ) {
			Collection<AdjacencyListGraph<V, E>.InternalEdge> adjListDst = getAdjacencyList().get(otherVertex);
			adjListDst.add(new InternalEdge(theEdge, aVertex));
		}
	}

	@Override
	// Dijsktra exige que los pesos sean positivos!!!
	public DijkstraPath<V, E> dijsktra(V source) {
		if (source == null || !existsVertex(source) )
			throw new IllegalArgumentException(Messages.getString("vertexParamError"));


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

	protected boolean existsVertex(V aVertex){
		return getAdjacencyList().containsKey(aVertex);
	}

	@Override
	public void printAllPaths(V startNode, V endNode) {
		if (startNode == null || !existsVertex(startNode) || endNode == null || !existsVertex(endNode) )
			throw new IllegalArgumentException(Messages.getString("vertexParamError"));

		if (acceptSelfLoop)
			throw new IllegalArgumentException(Messages.getString("printAllPathsNotDefinedForGraphsWithSlefLoops"));

		Set<V> visited= new HashSet<V>();
		ArrayList<V> path= new ArrayList<>();
		printAllPaths(startNode, endNode, visited, path);
	}


	public void printAllPaths(V startNode, V endNode, Set<V> visited, ArrayList<V> path ) {
		// proceso el nodo
		path.add(startNode);
		visited.add(startNode);

		// end?
		if (startNode.equals(endNode)) {
			System.out.println(path);

			// deshago aca porque no voy a pasar por flujo normal
			visited.remove(endNode);
			path.remove(endNode);
			return;
		}

		// go on
		Collection<InternalEdge> adjListOther = getAdjacencyList().get(startNode);
		for (InternalEdge internalEdge : adjListOther) {
			if (! visited.contains(internalEdge.target)) {
				printAllPaths(internalEdge.target, endNode, visited, path);
			}
		}

		// lo deshago
		visited.remove(startNode);
		path.remove(startNode);
	}

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
