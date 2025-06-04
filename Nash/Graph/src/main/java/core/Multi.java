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

		Collection<InternalEdge> adjListSrc = getAdjacencyList().get(aVertex);

		// creacion de ejes sin problema
		adjListSrc.add(new InternalEdge(theEdge, otherVertex));

		if (!isDirected) {
			Collection<InternalEdge> adjListDst = getAdjacencyList().get(otherVertex);
			adjListDst.add(new InternalEdge(theEdge, aVertex));
		}
	}

	public DijkstraPath<V,E> dijsktra(V source) {
		throw new RuntimeException(Messages.getString("dijkstraNotForMulti"));
	}

	@Override
	public void printAllPaths(V start, V end) {
		throw new RuntimeException(Messages.getString("printAllPathsNotDefinedForMulti"));
	}
}
