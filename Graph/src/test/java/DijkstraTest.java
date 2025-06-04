import core.*;
import core.GraphService.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for Dijkstra algorithm
 * Tests the shortest path finding functionality
 */
public class DijkstraTest {
    
    private GraphService<Character, WeightedEdge> weightedDirectedGraph;
    private GraphService<Character, WeightedEdge> weightedUndirectedGraph;
    private GraphService<Character, EmptyEdgeProp> unweightedGraph;
    private GraphService<Character, WeightedEdge> multiGraph;
    
    @BeforeEach
    public void setUp() {
        // Create a weighted directed graph for testing
        // A --(5)--> B --(2)--> D
        // |         |
        // (3)       (6)
        // |         |
        // v         v
        // C --(1)--> E
        weightedDirectedGraph = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.YES)
                .build();
        
        weightedDirectedGraph.addEdge('A', 'B', new WeightedEdge(5));
        weightedDirectedGraph.addEdge('A', 'C', new WeightedEdge(3));
        weightedDirectedGraph.addEdge('B', 'D', new WeightedEdge(2));
        weightedDirectedGraph.addEdge('B', 'E', new WeightedEdge(6));
        weightedDirectedGraph.addEdge('C', 'E', new WeightedEdge(1));
        
        // Create a weighted undirected graph
        // A ---(4)--- B ---(2)--- D
        // |           |
        // (7)         (3)
        // |           |
        // C ---(1)--- E
        weightedUndirectedGraph = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.YES)
                .build();
        
        weightedUndirectedGraph.addEdge('A', 'B', new WeightedEdge(4));
        weightedUndirectedGraph.addEdge('A', 'C', new WeightedEdge(7));
        weightedUndirectedGraph.addEdge('B', 'D', new WeightedEdge(2));
        weightedUndirectedGraph.addEdge('B', 'E', new WeightedEdge(3));
        weightedUndirectedGraph.addEdge('C', 'E', new WeightedEdge(1));
        
        // Create an unweighted graph (should throw exception)
        unweightedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        // Create a multi graph (should throw exception)
        multiGraph = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.YES)
                .build();
    }

    @Test
    public void testDijkstraDirectedGraph() {
        DijkstraPath<Character, WeightedEdge> result = weightedDirectedGraph.dijkstra('A');
        
        // Check distances from A
        assertEquals(0, result.getDistances().get('A').intValue(), "Distance from A to A should be 0");
        assertEquals(5, result.getDistances().get('B').intValue(), "Distance from A to B should be 5");
        assertEquals(3, result.getDistances().get('C').intValue(), "Distance from A to C should be 3");
        assertEquals(7, result.getDistances().get('D').intValue(), "Distance from A to D should be 7 (A->B->D)");
        assertEquals(4, result.getDistances().get('E').intValue(), "Distance from A to E should be 4 (A->C->E)");
        
        // Check some paths
        assertEquals("[A]", result.getShortestPathTo('A'), "Path from A to A");
        assertEquals("[A, B]", result.getShortestPathTo('B'), "Path from A to B");
        assertEquals("[A, C]", result.getShortestPathTo('C'), "Path from A to C");
        assertEquals("[A, B, D]", result.getShortestPathTo('D'), "Path from A to D");
        assertEquals("[A, C, E]", result.getShortestPathTo('E'), "Path from A to E");
    }

    @Test
    public void testDijkstraUndirectedGraph() {
        DijkstraPath<Character, WeightedEdge> result = weightedUndirectedGraph.dijkstra('A');
        
        // Check distances from A in undirected graph
        assertEquals(0, result.getDistances().get('A').intValue(), "Distance from A to A should be 0");
        assertEquals(4, result.getDistances().get('B').intValue(), "Distance from A to B should be 4");
        assertEquals(7, result.getDistances().get('C').intValue(), "Distance from A to C should be 7");
        assertEquals(6, result.getDistances().get('D').intValue(), "Distance from A to D should be 6 (A->B->D)");
        assertEquals(7, result.getDistances().get('E').intValue(), "Distance from A to E should be 7 (A->B->E)");
    }

    @Test
    public void testDijkstraWithDisconnectedVertex() {
        // Add an isolated vertex
        weightedDirectedGraph.addVertex('F');
        
        DijkstraPath<Character, WeightedEdge> result = weightedDirectedGraph.dijkstra('A');
        
        // Isolated vertex should have infinite distance
        assertEquals(Integer.MAX_VALUE, result.getDistances().get('F').intValue(), 
                    "Distance to isolated vertex should be infinity");
        assertEquals("[]", result.getShortestPathTo('F'), "Path to isolated vertex should be empty");
    }

    @Test
    public void testDijkstraFromDifferentSource() {
        DijkstraPath<Character, WeightedEdge> result = weightedDirectedGraph.dijkstra('C');
        
        // Check distances from C
        assertEquals(Integer.MAX_VALUE, result.getDistances().get('A').intValue(), "A unreachable from C");
        assertEquals(Integer.MAX_VALUE, result.getDistances().get('B').intValue(), "B unreachable from C");
        assertEquals(0, result.getDistances().get('C').intValue(), "Distance from C to C should be 0");
        assertEquals(Integer.MAX_VALUE, result.getDistances().get('D').intValue(), "D unreachable from C");
        assertEquals(1, result.getDistances().get('E').intValue(), "Distance from C to E should be 1");
    }

    @Test
    public void testDijkstraToString() {
        DijkstraPath<Character, WeightedEdge> result = weightedDirectedGraph.dijkstra('A');
        String output = result.toString();
        
        assertTrue(output.contains("0: [A]"), "Should contain source vertex with distance 0");
        assertTrue(output.contains("5: [A, B]"), "Should contain path to B");
        assertTrue(output.contains("3: [A, C]"), "Should contain path to C");
        assertTrue(output.contains("7: [A, B, D]"), "Should contain path to D");
        assertTrue(output.contains("4: [A, C, E]"), "Should contain path to E");
    }

    @Test
    public void testExceptionForUnweightedGraph() {
        unweightedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            unweightedGraph.dijkstra('A');
        });
        
        assertTrue(exception.getMessage().contains("Dijkstra algorithm requires a weighted graph"));
    }

    @Test
    public void testExceptionForMultiGraph() {
        multiGraph.addEdge('A', 'B', new WeightedEdge(5));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            multiGraph.dijkstra('A');
        });
        
        assertTrue(exception.getMessage().contains("Dijkstra algorithm is only allowed for simple graphs"));
    }

    @Test
    public void testExceptionForNullSource() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weightedDirectedGraph.dijkstra(null);
        });
        
        assertTrue(exception.getMessage().contains("Source vertex does not exist"));
    }

    @Test
    public void testExceptionForNonExistentSource() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            weightedDirectedGraph.dijkstra('Z');
        });
        
        assertTrue(exception.getMessage().contains("Source vertex does not exist"));
    }

    @Test
    public void testSingleVertexGraph() {
        GraphService<Character, WeightedEdge> singleVertexGraph = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.YES)
                .build();
        
        singleVertexGraph.addVertex('A');
        
        DijkstraPath<Character, WeightedEdge> result = singleVertexGraph.dijkstra('A');
        
        assertEquals(0, result.getDistances().get('A').intValue(), "Distance from A to itself should be 0");
        assertEquals("[A]", result.getShortestPathTo('A'), "Path should contain only A");
    }
} 