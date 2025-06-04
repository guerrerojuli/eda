import core.*;
import core.GraphService.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for numberOfEdges() method implementation
 * Tests different graph configurations: Simple/Multi, Directed/Undirected, with/without self-loops
 */
public class NumberOfEdgesTest {

    @Test
    public void testCasoDeUsoA_SimpleDirectedGraph() {
        // Caso de Uso A: Grafo simple dirigido sin self-loops
        GraphService<Character, EmptyEdgeProp> g = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();

        g.addEdge('E', 'B', new EmptyEdgeProp());
        g.addEdge('A', 'B', new EmptyEdgeProp());
        g.addEdge('F', 'B', new EmptyEdgeProp());
        g.addVertex('D');
        g.addVertex('G');
        g.addEdge('E', 'F', new EmptyEdgeProp());
        g.addEdge('F', 'A', new EmptyEdgeProp());
        g.addEdge('F', 'G', new EmptyEdgeProp());
        g.addEdge('U', 'G', new EmptyEdgeProp());
        g.addEdge('T', 'U', new EmptyEdgeProp());
        g.addEdge('C', 'G', new EmptyEdgeProp());

        assertEquals(9, g.numberOfEdges(), "Caso de Uso A: Debe tener 9 aristas");
    }

    @Test
    public void testCasoDeUsoB_SimpleDirectedGraphWithSelfLoop() {
        // Caso de Uso B: Grafo simple dirigido con self-loops
        GraphService<Character, EmptyEdgeProp> g = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.NO)
                .build();

        g.addEdge('E', 'B', new EmptyEdgeProp());
        g.addEdge('A', 'B', new EmptyEdgeProp());
        g.addEdge('F', 'B', new EmptyEdgeProp());
        g.addVertex('D');
        g.addVertex('G');
        g.addEdge('E', 'F', new EmptyEdgeProp());
        g.addEdge('F', 'A', new EmptyEdgeProp());
        g.addEdge('F', 'G', new EmptyEdgeProp());
        g.addEdge('U', 'G', new EmptyEdgeProp());
        g.addEdge('T', 'U', new EmptyEdgeProp());
        g.addEdge('C', 'G', new EmptyEdgeProp());
        g.addEdge('F', 'F', new EmptyEdgeProp()); // Self-loop

        assertEquals(10, g.numberOfEdges(), "Caso de Uso B: Debe tener 10 aristas (incluyendo self-loop)");
    }

    @Test
    public void testCasoDeUsoC_MultiDirectedGraphWithWeights() {
        // Caso de Uso C: Multigrafo dirigido con pesos y self-loops múltiples
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addEdge('E', 'B', new WeightedEdge(3));
        g.addEdge('A', 'B', new WeightedEdge(1));
        g.addEdge('F', 'B', new WeightedEdge(2));
        g.addVertex('D');
        g.addVertex('G');
        g.addEdge('E', 'F', new WeightedEdge(-2));
        g.addEdge('F', 'A', new WeightedEdge(8));
        g.addEdge('F', 'G', new WeightedEdge(2));
        g.addEdge('U', 'G', new WeightedEdge(-10));
        g.addEdge('T', 'U', new WeightedEdge(8));
        g.addEdge('C', 'G', new WeightedEdge(1));
        g.addEdge('G', 'U', new WeightedEdge(0));
        g.addEdge('F', 'F', new WeightedEdge(3)); // Self-loop 1
        g.addEdge('F', 'F', new WeightedEdge(2)); // Self-loop 2 (múltiple)

        assertEquals(12, g.numberOfEdges(), "Caso de Uso C: Debe tener 12 aristas (incluyendo múltiples self-loops)");
    }

    @Test
    public void testCasoDeUsoD_MultiDirectedGraphSmaller() {
        // Caso de Uso D: Multigrafo dirigido más pequeño
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addVertex('D');
        g.addVertex('G');
        g.addEdge('G', 'F', new WeightedEdge(2));
        g.addEdge('U', 'G', new WeightedEdge(-10));
        g.addEdge('U', 'G', new WeightedEdge(0)); // Múltiple arista
        g.addEdge('F', 'F', new WeightedEdge(3)); // Self-loop 1
        g.addEdge('F', 'F', new WeightedEdge(2)); // Self-loop 2

        assertEquals(5, g.numberOfEdges(), "Caso de Uso D: Debe tener 5 aristas");
    }

    @Test
    public void testUndirectedGraphEdgeCount() {
        // Test adicional: Grafo no dirigido para verificar que no se cuentan doble las aristas
        GraphService<Character, EmptyEdgeProp> g = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();

        g.addEdge('A', 'B', new EmptyEdgeProp());
        g.addEdge('B', 'C', new EmptyEdgeProp());
        g.addEdge('C', 'A', new EmptyEdgeProp());

        assertEquals(3, g.numberOfEdges(), "Grafo no dirigido: 3 aristas no deben contarse doble");
    }

    @Test
    public void testUndirectedGraphWithSelfLoops() {
        // Test adicional: Grafo no dirigido con self-loops
        GraphService<Character, EmptyEdgeProp> g = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.NO)
                .build();

        g.addEdge('A', 'B', new EmptyEdgeProp());
        g.addEdge('B', 'C', new EmptyEdgeProp());
        g.addEdge('A', 'A', new EmptyEdgeProp()); // Self-loop

        assertEquals(3, g.numberOfEdges(), "Grafo no dirigido con self-loop: debe contar correctamente");
    }

    @Test
    public void testEmptyGraph() {
        // Test caso límite: Grafo vacío
        GraphService<Character, EmptyEdgeProp> g = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();

        assertEquals(0, g.numberOfEdges(), "Grafo vacío debe tener 0 aristas");
    }

    @Test
    public void testGraphWithOnlyVertices() {
        // Test caso límite: Grafo solo con vértices, sin aristas
        GraphService<Character, EmptyEdgeProp> g = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();

        g.addVertex('A');
        g.addVertex('B');
        g.addVertex('C');

        assertEquals(0, g.numberOfEdges(), "Grafo con solo vértices debe tener 0 aristas");
    }

    @Test
    public void testMultiUndirectedGraphWithMultipleSelfLoops() {
        // Test complejo: Multigrafo no dirigido con múltiples self-loops
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addEdge('A', 'B', new WeightedEdge(1));
        g.addEdge('A', 'B', new WeightedEdge(2)); // Múltiple
        g.addEdge('A', 'A', new WeightedEdge(5)); // Self-loop 1
        g.addEdge('A', 'A', new WeightedEdge(6)); // Self-loop 2
        g.addEdge('B', 'C', new WeightedEdge(3));

        assertEquals(5, g.numberOfEdges(), "Multigrafo no dirigido con múltiples self-loops");
    }
} 