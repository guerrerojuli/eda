import core.*;
import core.GraphService.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for degree(), inDegree(), and outDegree() methods
 * Tests different graph configurations and verifies exception handling
 */
public class DegreeMethodsTest {

    @Test
    public void testCasoDeUsoA_DegreeOnDirectedGraphThrowsException() {
        // Caso de Uso A: degree() en grafo dirigido debe lanzar excepción
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
        g.addEdge('U', 'G', new WeightedEdge(0));
        g.addEdge('F', 'F', new WeightedEdge(3));
        g.addEdge('F', 'F', new WeightedEdge(2));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            g.degree('G');
        });
        
        assertTrue(exception.getMessage().contains("degree() method is only applicable to undirected graphs"),
                "Debe lanzar excepción indicando que degree() solo aplica para grafos no dirigidos");
    }

    @Test
    public void testCasoDeUsoB_InDegreeAndOutDegreeDirectedGraph() {
        // Caso de Uso B: inDegree y outDegree en grafo dirigido
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
        g.addEdge('U', 'G', new WeightedEdge(0));
        g.addEdge('F', 'F', new WeightedEdge(3));
        g.addEdge('F', 'F', new WeightedEdge(2));

        assertEquals(2, g.inDegree('G'), "inDegree de G debe ser 2");
        assertEquals(1, g.outDegree('G'), "outDegree de G debe ser 1");
        assertEquals(3, g.inDegree('F'), "inDegree de F debe ser 3 (1 desde G + 2 self-loops = 1+2=3)");
        assertEquals(2, g.outDegree('F'), "outDegree de F debe ser 2 (2 self-loops = 2)");
    }

    @Test
    public void testCasoDeUsoC_DegreeUndirectedGraph() {
        // Caso de Uso C: degree en grafo no dirigido
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.UNDIRECTED)
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
        g.addEdge('F', 'F', new WeightedEdge(3));
        g.addEdge('F', 'F', new WeightedEdge(2));

        assertEquals(4, g.degree('G'), "degree de G debe ser 4");
        assertEquals(8, g.degree('F'), "degree de F debe ser 8");
    }

    @Test
    public void testInDegreeOutDegreeOnUndirectedGraphThrowsException() {
        // Test adicional: inDegree y outDegree en grafo no dirigido deben lanzar excepción
        GraphService<Character, EmptyEdgeProp> g = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();

        g.addVertex('A');

        RuntimeException inDegreeException = assertThrows(RuntimeException.class, () -> {
            g.inDegree('A');
        });
        assertTrue(inDegreeException.getMessage().contains("inDegree() method is only applicable to directed graphs"));

        RuntimeException outDegreeException = assertThrows(RuntimeException.class, () -> {
            g.outDegree('A');
        });
        assertTrue(outDegreeException.getMessage().contains("outDegree() method is only applicable to directed graphs"));
    }

    @Test
    public void testNonExistentVertexThrowsException() {
        // Test adicional: vértice inexistente debe lanzar excepción
        GraphService<Character, EmptyEdgeProp> g = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            g.inDegree('X');
        });
        assertTrue(exception.getMessage().contains("Vertex does not exist"));
    }

    @Test
    public void testSelfLoopContributionInDirectedGraph() {
        // Test específico: verificar que self-loops contribuyen doble en grafos dirigidos
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addEdge('A', 'A', new WeightedEdge(1)); // Self-loop

        assertEquals(1, g.inDegree('A'), "Self-loop debe contribuir 1 al inDegree");
        assertEquals(1, g.outDegree('A'), "Self-loop debe contribuir 1 al outDegree");
    }

    @Test
    public void testSelfLoopContributionInUndirectedGraph() {
        // Test específico: verificar que self-loops contribuyen doble en grafos no dirigidos
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addEdge('A', 'A', new WeightedEdge(1)); // Self-loop

        assertEquals(2, g.degree('A'), "Self-loop debe contribuir 2 al degree en grafo no dirigido");
    }

    @Test
    public void testSimpleDirectedGraphDegrees() {
        // Test adicional: grafo dirigido simple sin self-loops
        GraphService<Character, EmptyEdgeProp> g = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();

        g.addEdge('A', 'B', new EmptyEdgeProp());
        g.addEdge('A', 'C', new EmptyEdgeProp());
        g.addEdge('B', 'C', new EmptyEdgeProp());

        assertEquals(0, g.inDegree('A'), "A no tiene aristas entrantes");
        assertEquals(2, g.outDegree('A'), "A tiene 2 aristas salientes");
        assertEquals(1, g.inDegree('B'), "B tiene 1 arista entrante");
        assertEquals(1, g.outDegree('B'), "B tiene 1 arista saliente");
        assertEquals(2, g.inDegree('C'), "C tiene 2 aristas entrantes");
        assertEquals(0, g.outDegree('C'), "C no tiene aristas salientes");
    }
} 