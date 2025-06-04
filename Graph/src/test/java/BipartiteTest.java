import core.*;
import core.GraphService.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for isBipartite method
 * Tests the bipartite detection functionality
 */
public class BipartiteTest {
    
    private GraphService<Character, EmptyEdgeProp> simpleUndirectedGraph;
    private GraphService<Character, EmptyEdgeProp> simpleDirectedGraph;
    private GraphService<Integer, EmptyEdgeProp> integerGraph;
    
    @BeforeEach
    public void setUp() {
        // Create a simple undirected graph for testing
        simpleUndirectedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        // Create a simple directed graph for testing
        simpleDirectedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        // Create a graph with integer vertices
        integerGraph = new GraphBuilder<Integer, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
    }

    @Test
    public void testEmptyGraphIsBipartite() {
        assertTrue(simpleUndirectedGraph.isBipartite(), "Empty graph should be bipartite");
    }

    @Test
    public void testSingleVertexIsBipartite() {
        simpleUndirectedGraph.addVertex('A');
        assertTrue(simpleUndirectedGraph.isBipartite(), "Single vertex graph should be bipartite");
    }

    @Test
    public void testTwoVerticesNoEdgeIsBipartite() {
        simpleUndirectedGraph.addVertex('A');
        simpleUndirectedGraph.addVertex('B');
        assertTrue(simpleUndirectedGraph.isBipartite(), "Two isolated vertices should be bipartite");
    }

    @Test
    public void testTwoVerticesWithEdgeIsBipartite() {
        simpleUndirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        assertTrue(simpleUndirectedGraph.isBipartite(), "Two vertices with one edge should be bipartite");
    }

    @Test
    public void testTriangleIsNotBipartite() {
        // Create a triangle: A-B-C-A
        simpleUndirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('C', 'A', new EmptyEdgeProp());
        
        assertFalse(simpleUndirectedGraph.isBipartite(), "Triangle (odd cycle) should not be bipartite");
    }

    @Test
    public void testSquareIsBipartite() {
        // Create a square: A-B-C-D-A
        simpleUndirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('D', 'A', new EmptyEdgeProp());
        
        assertTrue(simpleUndirectedGraph.isBipartite(), "Square (even cycle) should be bipartite");
    }

    @Test
    public void testPentagonIsNotBipartite() {
        // Create a pentagon: A-B-C-D-E-A
        simpleUndirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('D', 'E', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('E', 'A', new EmptyEdgeProp());
        
        assertFalse(simpleUndirectedGraph.isBipartite(), "Pentagon (odd cycle) should not be bipartite");
    }

    @Test
    public void testStarGraphIsBipartite() {
        // Create a star graph: center connected to all others
        // A is center, connected to B, C, D, E
        simpleUndirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('A', 'D', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('A', 'E', new EmptyEdgeProp());
        
        assertTrue(simpleUndirectedGraph.isBipartite(), "Star graph should be bipartite");
    }

    @Test
    public void testTreeIsBipartite() {
        // Create a tree structure
        simpleUndirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('B', 'D', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('B', 'E', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('C', 'F', new EmptyEdgeProp());
        
        assertTrue(simpleUndirectedGraph.isBipartite(), "Tree should be bipartite");
    }

    @Test
    public void testDisconnectedGraphWithBipartiteComponents() {
        // Two separate edges: A-B and C-D
        simpleUndirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        
        assertTrue(simpleUndirectedGraph.isBipartite(), "Disconnected bipartite components should be bipartite");
    }

    @Test
    public void testDisconnectedGraphWithNonBipartiteComponent() {
        // One bipartite component (A-B) and one non-bipartite component (triangle C-D-E)
        simpleUndirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('D', 'E', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('E', 'C', new EmptyEdgeProp());
        
        assertFalse(simpleUndirectedGraph.isBipartite(), "Graph with non-bipartite component should not be bipartite");
    }

    @Test
    public void testDirectedGraphBipartite() {
        // Create a directed bipartite graph: A->B, C->B, A->D, C->D
        simpleDirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleDirectedGraph.addEdge('C', 'B', new EmptyEdgeProp());
        simpleDirectedGraph.addEdge('A', 'D', new EmptyEdgeProp());
        simpleDirectedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        
        assertTrue(simpleDirectedGraph.isBipartite(), "Directed bipartite graph should be bipartite");
    }

    @Test
    public void testDirectedGraphWithCycle() {
        // Create a directed triangle: A->B->C->A
        simpleDirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleDirectedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        simpleDirectedGraph.addEdge('C', 'A', new EmptyEdgeProp());
        
        assertFalse(simpleDirectedGraph.isBipartite(), "Directed odd cycle should not be bipartite");
    }

    @Test
    public void testComplexBipartiteGraph() {
        // Create a complete bipartite graph K2,3
        // Set 1: {A, B}, Set 2: {C, D, E}
        // Every vertex in set 1 connects to every vertex in set 2
        simpleUndirectedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('A', 'D', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('A', 'E', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('B', 'D', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('B', 'E', new EmptyEdgeProp());
        
        assertTrue(simpleUndirectedGraph.isBipartite(), "Complete bipartite graph K2,3 should be bipartite");
    }

    @Test
    public void testIntegerVertices() {
        // Test with integer vertices to ensure generic type compatibility
        integerGraph.addEdge(1, 2, new EmptyEdgeProp());
        integerGraph.addEdge(2, 3, new EmptyEdgeProp());
        integerGraph.addEdge(3, 4, new EmptyEdgeProp());
        integerGraph.addEdge(4, 1, new EmptyEdgeProp()); // Even cycle
        
        assertTrue(integerGraph.isBipartite(), "Even cycle with integer vertices should be bipartite");
        
        // Add one more edge to make odd cycle
        integerGraph.addEdge(1, 3, new EmptyEdgeProp());
        
        assertFalse(integerGraph.isBipartite(), "Graph with odd cycle should not be bipartite");
    }
} 