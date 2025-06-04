import core.*;
import core.GraphService.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Test cases for printAllPaths method
 * Tests the functionality and exception handling for finding all paths
 */
public class AllPathsTest {
    
    private GraphService<Character, EmptyEdgeProp> simpleDirectedGraph;
    private GraphService<Character, EmptyEdgeProp> simpleUndirectedGraph;
    private GraphService<Character, EmptyEdgeProp> multiGraph;
    private GraphService<Character, EmptyEdgeProp> selfLoopGraph;
    
    @BeforeEach
    public void setUp() {
        // Create a simple directed graph for testing
        // A -> B -> D
        // |    |
        // v    v
        // C -> E
        simpleDirectedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        simpleDirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleDirectedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        simpleDirectedGraph.addEdge('B', 'D', new EmptyEdgeProp());
        simpleDirectedGraph.addEdge('B', 'E', new EmptyEdgeProp());
        simpleDirectedGraph.addEdge('C', 'E', new EmptyEdgeProp());
        
        // Create a simple undirected graph
        // A --- B --- D
        // |     |
        // C --- E
        simpleUndirectedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        simpleUndirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('B', 'D', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('B', 'E', new EmptyEdgeProp());
        simpleUndirectedGraph.addEdge('C', 'E', new EmptyEdgeProp());
        
        // Create a multi graph (should throw exception)
        multiGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        // Create a graph with self-loops (should throw exception)
        selfLoopGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.NO)
                .build();
    }

    @Test
    public void testPrintAllPathsDirectedGraph() {
        // Capture System.out output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        
        try {
            simpleDirectedGraph.printAllPaths('A', 'E');
            String output = baos.toString().trim();
            
            assertTrue(output.contains("All paths from A to E:"), "Output should contain header");
            assertTrue(output.contains("A -> B -> E"), "Should contain path A -> B -> E");
            assertTrue(output.contains("A -> C -> E"), "Should contain path A -> C -> E");
            assertTrue(output.contains("Total paths found: 2"), "Should find exactly 2 paths");
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testPrintAllPathsUndirectedGraph() {
        // Capture System.out output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        
        try {
            simpleUndirectedGraph.printAllPaths('A', 'D');
            String output = baos.toString().trim();
            
            assertTrue(output.contains("All paths from A to D:"), "Output should contain header");
            assertTrue(output.contains("A -> B -> D"), "Should contain direct path A -> B -> D");
            // In undirected graph, there should be multiple paths due to bidirectional edges
            assertTrue(output.contains("Total paths found:"), "Should find paths");
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testNoPathExists() {
        // Create a disconnected graph
        GraphService<Character, EmptyEdgeProp> disconnectedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        disconnectedGraph.addVertex('A');
        disconnectedGraph.addVertex('B'); // Isolated vertex
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        
        try {
            disconnectedGraph.printAllPaths('A', 'B');
            String output = baos.toString().trim();
            
            assertTrue(output.contains("No paths found from A to B"), "Should indicate no paths found");
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testSameStartAndEnd() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        
        try {
            simpleDirectedGraph.printAllPaths('A', 'A');
            String output = baos.toString().trim();
            
            assertTrue(output.contains("All paths from A to A:"), "Output should contain header");
            assertTrue(output.contains("Path 1: A"), "Should contain trivial path A");
            assertTrue(output.contains("Total paths found: 1"), "Should find exactly 1 path");
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testExceptionForMultiGraph() {
        multiGraph.addEdge('A', 'B', new EmptyEdgeProp());
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            multiGraph.printAllPaths('A', 'B');
        });
        
        assertTrue(exception.getMessage().contains("printAllPaths is not defined for Multigraphs"));
    }

    @Test
    public void testExceptionForSelfLoopGraph() {
        selfLoopGraph.addEdge('A', 'B', new EmptyEdgeProp());
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            selfLoopGraph.printAllPaths('A', 'B');
        });
        
        assertTrue(exception.getMessage().contains("printAllPaths is not defined for graphs with self-loops"));
    }

    @Test
    public void testExceptionForNullVertices() {
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> {
            simpleDirectedGraph.printAllPaths(null, 'B');
        });
        assertTrue(exception1.getMessage().contains("Start and end vertices cannot be null"));

        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> {
            simpleDirectedGraph.printAllPaths('A', null);
        });
        assertTrue(exception2.getMessage().contains("Start and end vertices cannot be null"));

        RuntimeException exception3 = assertThrows(RuntimeException.class, () -> {
            simpleDirectedGraph.printAllPaths(null, null);
        });
        assertTrue(exception3.getMessage().contains("Start and end vertices cannot be null"));
    }

    @Test
    public void testExceptionForNonExistentVertices() {
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> {
            simpleDirectedGraph.printAllPaths('Z', 'B');
        });
        assertTrue(exception1.getMessage().contains("Start vertex does not exist"));

        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> {
            simpleDirectedGraph.printAllPaths('A', 'Z');
        });
        assertTrue(exception2.getMessage().contains("End vertex does not exist"));
    }

    @Test
    public void testComplexPathFinding() {
        // Create a more complex graph with multiple possible paths
        GraphService<Character, EmptyEdgeProp> complexGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        // Create a diamond pattern: A -> B -> D and A -> C -> D
        // Plus additional connections: B -> C and C -> B for more paths
        complexGraph.addEdge('A', 'B', new EmptyEdgeProp());
        complexGraph.addEdge('A', 'C', new EmptyEdgeProp());
        complexGraph.addEdge('B', 'D', new EmptyEdgeProp());
        complexGraph.addEdge('C', 'D', new EmptyEdgeProp());
        complexGraph.addEdge('B', 'C', new EmptyEdgeProp()); // Additional connection
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        
        try {
            complexGraph.printAllPaths('A', 'D');
            String output = baos.toString().trim();
            
            assertTrue(output.contains("All paths from A to D:"), "Output should contain header");
            assertTrue(output.contains("A -> B -> D"), "Should contain path A -> B -> D");
            assertTrue(output.contains("A -> C -> D"), "Should contain path A -> C -> D");
            assertTrue(output.contains("A -> B -> C -> D"), "Should contain path A -> B -> C -> D");
            assertTrue(output.contains("Total paths found: 3"), "Should find exactly 3 paths");
        } finally {
            System.setOut(originalOut);
        }
    }
} 