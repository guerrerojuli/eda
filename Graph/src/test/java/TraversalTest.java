import core.*;
import core.GraphService.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for BFS and DFS traversal methods
 * Tests both print methods and iterable methods
 */
public class TraversalTest {
    
    private GraphService<Character, EmptyEdgeProp> directedGraph;
    private GraphService<Character, EmptyEdgeProp> undirectedGraph;
    
    @BeforeEach
    public void setUp() {
        // Create a directed graph for testing
        // A -> B -> D
        // |    |
        // v    v
        // C    E
        directedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        directedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        directedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        directedGraph.addEdge('B', 'D', new EmptyEdgeProp());
        directedGraph.addEdge('B', 'E', new EmptyEdgeProp());
        
        // Create an undirected graph for testing
        // A --- B --- D
        // |     |
        // C --- E
        undirectedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        undirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        undirectedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        undirectedGraph.addEdge('B', 'D', new EmptyEdgeProp());
        undirectedGraph.addEdge('B', 'E', new EmptyEdgeProp());
        undirectedGraph.addEdge('C', 'E', new EmptyEdgeProp());
    }

    @Test
    public void testPrintBFSDirectedGraph() {
        // Capture System.out output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        
        try {
            directedGraph.printBFS('A');
            String output = baos.toString().trim();
            
            assertTrue(output.startsWith("BFS from A:"), "Output should start with 'BFS from A:'");
            assertTrue(output.contains("A"), "Output should contain vertex A");
            assertTrue(output.contains("B"), "Output should contain vertex B");
            assertTrue(output.contains("C"), "Output should contain vertex C");
            assertTrue(output.contains("D"), "Output should contain vertex D");
            assertTrue(output.contains("E"), "Output should contain vertex E");
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testPrintDFSDirectedGraph() {
        // Capture System.out output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        
        try {
            directedGraph.printDFS('A');
            String output = baos.toString().trim();
            
            assertTrue(output.startsWith("DFS from A:"), "Output should start with 'DFS from A:'");
            assertTrue(output.contains("A"), "Output should contain vertex A");
            assertTrue(output.contains("B"), "Output should contain vertex B");
            assertTrue(output.contains("C"), "Output should contain vertex C");
            assertTrue(output.contains("D"), "Output should contain vertex D");
            assertTrue(output.contains("E"), "Output should contain vertex E");
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testGetBFSDirectedGraph() {
        List<Character> bfsResult = new ArrayList<>();
        for (Character vertex : directedGraph.getBFS('A')) {
            bfsResult.add(vertex);
        }
        
        assertEquals(5, bfsResult.size(), "BFS should visit all 5 vertices");
        assertEquals('A', bfsResult.get(0), "BFS should start with vertex A");
        assertTrue(bfsResult.contains('B'), "BFS should contain vertex B");
        assertTrue(bfsResult.contains('C'), "BFS should contain vertex C");
        assertTrue(bfsResult.contains('D'), "BFS should contain vertex D");
        assertTrue(bfsResult.contains('E'), "BFS should contain vertex E");
        
        // A should come before its neighbors B and C
        int indexA = bfsResult.indexOf('A');
        int indexB = bfsResult.indexOf('B');
        int indexC = bfsResult.indexOf('C');
        assertTrue(indexA < indexB, "A should come before B in BFS");
        assertTrue(indexA < indexC, "A should come before C in BFS");
    }

    @Test
    public void testGetDFSDirectedGraph() {
        List<Character> dfsResult = new ArrayList<>();
        for (Character vertex : directedGraph.getDFS('A')) {
            dfsResult.add(vertex);
        }
        
        assertEquals(5, dfsResult.size(), "DFS should visit all 5 vertices");
        assertEquals('A', dfsResult.get(0), "DFS should start with vertex A");
        assertTrue(dfsResult.contains('B'), "DFS should contain vertex B");
        assertTrue(dfsResult.contains('C'), "DFS should contain vertex C");
        assertTrue(dfsResult.contains('D'), "DFS should contain vertex D");
        assertTrue(dfsResult.contains('E'), "DFS should contain vertex E");
    }

    @Test
    public void testBFSUndirectedGraph() {
        List<Character> bfsResult = new ArrayList<>();
        for (Character vertex : undirectedGraph.getBFS('A')) {
            bfsResult.add(vertex);
        }
        
        assertEquals(5, bfsResult.size(), "BFS should visit all 5 vertices in undirected graph");
        assertEquals('A', bfsResult.get(0), "BFS should start with vertex A");
        assertTrue(bfsResult.contains('B'), "BFS should contain vertex B");
        assertTrue(bfsResult.contains('C'), "BFS should contain vertex C");
        assertTrue(bfsResult.contains('D'), "BFS should contain vertex D");
        assertTrue(bfsResult.contains('E'), "BFS should contain vertex E");
    }

    @Test
    public void testDFSUndirectedGraph() {
        List<Character> dfsResult = new ArrayList<>();
        for (Character vertex : undirectedGraph.getDFS('A')) {
            dfsResult.add(vertex);
        }
        
        assertEquals(5, dfsResult.size(), "DFS should visit all 5 vertices in undirected graph");
        assertEquals('A', dfsResult.get(0), "DFS should start with vertex A");
        assertTrue(dfsResult.contains('B'), "DFS should contain vertex B");
        assertTrue(dfsResult.contains('C'), "DFS should contain vertex C");
        assertTrue(dfsResult.contains('D'), "DFS should contain vertex D");
        assertTrue(dfsResult.contains('E'), "DFS should contain vertex E");
    }

    @Test
    public void testTraversalWithNonExistentVertex() {
        // Test all methods with non-existent vertex
        RuntimeException bfsException = assertThrows(RuntimeException.class, () -> {
            directedGraph.printBFS('Z');
        });
        assertTrue(bfsException.getMessage().contains("Vertex does not exist"));

        RuntimeException dfsException = assertThrows(RuntimeException.class, () -> {
            directedGraph.printDFS('Z');
        });
        assertTrue(dfsException.getMessage().contains("Vertex does not exist"));

        RuntimeException getBfsException = assertThrows(RuntimeException.class, () -> {
            directedGraph.getBFS('Z');
        });
        assertTrue(getBfsException.getMessage().contains("Vertex does not exist"));

        RuntimeException getDfsException = assertThrows(RuntimeException.class, () -> {
            directedGraph.getDFS('Z');
        });
        assertTrue(getDfsException.getMessage().contains("Vertex does not exist"));
    }

    @Test
    public void testTraversalWithNullVertex() {
        // Test all methods with null vertex
        RuntimeException bfsException = assertThrows(RuntimeException.class, () -> {
            directedGraph.printBFS(null);
        });
        assertTrue(bfsException.getMessage().contains("Vertex does not exist"));

        RuntimeException dfsException = assertThrows(RuntimeException.class, () -> {
            directedGraph.printDFS(null);
        });
        assertTrue(dfsException.getMessage().contains("Vertex does not exist"));

        RuntimeException getBfsException = assertThrows(RuntimeException.class, () -> {
            directedGraph.getBFS(null);
        });
        assertTrue(getBfsException.getMessage().contains("Vertex does not exist"));

        RuntimeException getDfsException = assertThrows(RuntimeException.class, () -> {
            directedGraph.getDFS(null);
        });
        assertTrue(getDfsException.getMessage().contains("Vertex does not exist"));
    }

    @Test
    public void testSingleVertexTraversal() {
        // Create a graph with a single vertex
        GraphService<Character, EmptyEdgeProp> singleVertexGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        singleVertexGraph.addVertex('X');
        
        List<Character> bfsResult = new ArrayList<>();
        for (Character vertex : singleVertexGraph.getBFS('X')) {
            bfsResult.add(vertex);
        }
        assertEquals(1, bfsResult.size(), "BFS of single vertex should return one element");
        assertEquals('X', bfsResult.get(0), "BFS should return the single vertex");
        
        List<Character> dfsResult = new ArrayList<>();
        for (Character vertex : singleVertexGraph.getDFS('X')) {
            dfsResult.add(vertex);
        }
        assertEquals(1, dfsResult.size(), "DFS of single vertex should return one element");
        assertEquals('X', dfsResult.get(0), "DFS should return the single vertex");
    }

    @Test
    public void testDisconnectedGraphTraversal() {
        // Create a graph with disconnected components
        GraphService<Character, EmptyEdgeProp> disconnectedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        disconnectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        disconnectedGraph.addVertex('C'); // Isolated vertex
        
        List<Character> bfsResult = new ArrayList<>();
        for (Character vertex : disconnectedGraph.getBFS('A')) {
            bfsResult.add(vertex);
        }
        assertEquals(2, bfsResult.size(), "BFS from A should only visit A and B, not isolated C");
        assertTrue(bfsResult.contains('A'), "BFS should contain A");
        assertTrue(bfsResult.contains('B'), "BFS should contain B");
        assertFalse(bfsResult.contains('C'), "BFS should not reach isolated vertex C");
        
        List<Character> dfsResult = new ArrayList<>();
        for (Character vertex : disconnectedGraph.getDFS('A')) {
            dfsResult.add(vertex);
        }
        assertEquals(2, dfsResult.size(), "DFS from A should only visit A and B, not isolated C");
        assertTrue(dfsResult.contains('A'), "DFS should contain A");
        assertTrue(dfsResult.contains('B'), "DFS should contain B");
        assertFalse(dfsResult.contains('C'), "DFS should not reach isolated vertex C");
    }
} 