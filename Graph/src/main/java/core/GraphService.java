/**
 * 
 */
package core;



import java.util.Collection;


// same interface for graph, digraph, multigraph, weighted graph, etc

// V participa de hashing. Redefinir equals y hashcode para que detecte
// correctamente repetidos segun se desee


// E precisa la redefinicion de equals para que en remove lo encuentre 
// y lo pueda borrar. Actualmente no participa de un hashing. Esta encapsulado
// dentro de un objeto InternalEdge que lo contiene junto con el V destino. 
// Esa clase InternalEdge s� tiene hashcode y equals implementado.

public interface GraphService<V,E> {
	
	enum Multiplicity { SIMPLE, MULTIPLE};
	enum EdgeMode { UNDIRECTED, DIRECTED};
	enum SelfLoop { NO, YES};
	enum Weight{ NO, YES	};
	enum Storage { SPARSE, DENSE };

	// devuelve caracteristicas de la forma en que fue creado
	public String getType();
		
	
	// if exists lo ignora
	// else generate a new vertex 
	public void addVertex(V aVertex);

	
	public int numberOfVertices();
	
	
	public Collection<V> getVertices();

	
	// parameters cannot be null
	// if any of the vertices is not present, the node is created automatically

	// in the case of a weighted graph, E might implements the method double getWeight()
	// otherwise an exception will be thrown
		
	// if the graph is not "multi" and there exists other edge with same source and target, 
	// throws exception
	public void addEdge(V aVertex, V otherVertex, E theEdge);

	
	public int numberOfEdges();
	
	

	// if the vertex does not exists returns false
	// else remove the vertex and its incident edges and returns true
	public boolean removeVertex(V aVertex);
	
	// removes the only edge that connects aVertex and otherVertex in a simple/default graph
	// if the edge does not exists returns false
	// else returns removes it and returns true
	// the method is not allowed in multi graph/digraph=> throws exception
	public boolean removeEdge(V aVertex, V otherVertex);

	
	
	// removes all the edges that connects aVertex and otherVertex in multi graph/digraph with theEdge properties
	// removes the only edge that connects aVertex and otherVertex in a simple graph/digraph, 
	// without considering the edge properties
	// if the edge does not exists returns false
	// else returns returns true
	public boolean removeEdge(V aVertex, V otherVertex, E theEdge);

	
	// dump all the informations and structure of vertexes and edges in any order
	public void dump();
	
	
	// if directed or vertex does not exists: throw exception
	// if self loop contributes twice
	public int degree(V vertex);
	
	// if undirected or vertex does not exists: throw exception
	// if self loop contributes twice
	public int inDegree(V vertex);
	
	// if undirected or vertex does not exists: throw exception
	// if self loop contributes twice
	public int outDegree(V vertex);
	
	// BFS traversal starting from the given vertex
	// if vertex does not exist: throw exception
	public void printBFS(V vertex);
	
	// DFS traversal starting from the given vertex  
	// if vertex does not exist: throw exception
	public void printDFS(V vertex);
	
	// Returns an iterable over vertices in BFS order starting from the given vertex
	// if vertex does not exist: throw exception
	public Iterable<V> getBFS(V vertex);
	
	// Returns an iterable over vertices in DFS order starting from the given vertex (iterative implementation)
	// if vertex does not exist: throw exception
	public Iterable<V> getDFS(V vertex);
	
	// Prints all possible paths from start to end vertex
	// Only for simple graphs without self-loops, otherwise throw exception
	// if start or end vertex does not exist: throw exception
	public void printAllPaths(V start, V end);
	
	// only for simple graph/digraph
	// multi: throw exception
	public DijkstraPath<V,E> dijkstra(V source);
	
	// Returns true if the graph is bipartite, false otherwise
	// A graph is bipartite if its vertices can be colored with two colors
	// such that no two adjacent vertices have the same color
	public boolean isBipartite();
	
	// Returns true if the graph contains at least one cycle, false otherwise
	// For directed graphs: detects directed cycles using DFS with recursion stack
	// For undirected graphs: detects cycles using DFS with parent tracking
	public boolean hasCycle();
	
}
