package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AdjacencyListGraph<V, E> implements GraphService<V, E> {

    private boolean isSimple;
    protected boolean isDirected;
    private boolean acceptSelfLoop;
    private boolean isWeighted;
    private Map<V, Collection<InternalEdge>> adjacencyList = new HashMap<>();

    protected Map<V, Collection<AdjacencyListGraph<V, E>.InternalEdge>> getAdjacencyList() {
        return adjacencyList;
    }

    public Collection<V> getVertices() {
        return getAdjacencyList().keySet();
    }

    protected AdjacencyListGraph(boolean isSimple, boolean isDirected, boolean acceptSelfLoop, boolean isWeighted) {
        this.isSimple = isSimple;
        this.isDirected = isDirected;
        this.acceptSelfLoop = acceptSelfLoop;
        this.isWeighted = isWeighted;
    }

    public int connectedComponentsQty() {
        // Si el grafo es dirigido, lanzar excepción
        if (isDirected) {
            throw new RuntimeException("Cannot calculate connected components for directed graph");
        }

        // Conjunto para llevar el registro de vértices visitados
        Set<V> visited = new HashSet<>();
        int componentCount = 0;

        // Para cada vértice no visitado, realizar DFS
        for (V vertex : getVertices()) {
            if (!visited.contains(vertex)) {
                // Nuevo componente encontrado
                componentCount++;
                dfs(vertex, visited);
            }
        }

        return componentCount;
    }

    /**
     * Realiza DFS desde un vértice dado, marcando todos los vértices alcanzables
     * como visitados
     */
    private void dfs(V vertex, Set<V> visited) {
        visited.add(vertex);

        // Obtener los vecinos del vértice actual
        Collection<InternalEdge> neighbors = getAdjacencyList().get(vertex);
        if (neighbors != null) {
            for (InternalEdge edge : neighbors) {
                V neighbor = edge.target;
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, visited);
                }
            }
        }
    }

    public void addVertex(V aVertex) {

        if (aVertex == null)
            throw new IllegalArgumentException("addVertexParamCannotBeNull");

        // no edges yet
        getAdjacencyList().putIfAbsent(aVertex,
                new ArrayList<InternalEdge>());
    }

    public void addEdge(V aVertex, V otherVertex, E theEdge) {

        // validation!!!!
        if (aVertex == null || otherVertex == null || theEdge == null)
            throw new IllegalArgumentException("addEdgeParamCannotBeNull");

        // es con peso? debe tener implementado el metodo double getWeight()
        if (isWeighted) {
            // reflection
            Class<? extends Object> c = theEdge.getClass();
            try {
                c.getDeclaredMethod("getWeight");
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(
                        "Graph is weighted but the method double getWeighed() is not declared in theEdge");
            }
        }

        if (!acceptSelfLoop && aVertex.equals(otherVertex)) {
            throw new RuntimeException(String.format("Graph does not accept self loops between %s and %s",
                    aVertex, otherVertex));
        }

        // if any of the vertex is not presented, the node is created automatically
        addVertex(aVertex);
        addVertex(otherVertex);
    }

    class InternalEdge {
        E edge;
        V target;

        InternalEdge(E propEdge, V target) {
            this.target = target;
            this.edge = propEdge;
        }
    }
}
