package core;


import java.util.Collection;





public class Multi<V,E> extends AdjacencyListGraph<V,E> {

	protected Multi(boolean isDirected, boolean acceptSelfLoops, boolean isWeighted) {
		super(false, isDirected, acceptSelfLoops, isWeighted);
		
	}
	
	
	
	@Override
	public void addEdge(V aVertex, V otherVertex, E theEdge) {

		// validacion y creacion de vertices si fuera necesario
		super.addEdge(aVertex, otherVertex, theEdge);

		// Multi graph allows multiple edges, so simply add without checking duplicates
		Collection<InternalEdge> adjacentEdges = getAdjacencyList().get(aVertex);
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
	public DijkstraPath<V,E> dijkstra(V source) {
		throw new RuntimeException("Dijkstra algorithm is only allowed for simple graphs");
	}
	
	@Override
	public void printAllPaths(V start, V end) {
		throw new RuntimeException(Messages.getString("printAllPathsNotDefinedForMulti"));
	}
	
	
}
