import core.*;
import core.GraphService.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for hasCycle method
 * Tests cycle detection functionality for both directed and undirected graphs
 */
public class CycleDetectionTest {
    
    private GraphService<Character, EmptyEdgeProp> undirectedGraph;
    private GraphService<Character, EmptyEdgeProp> directedGraph;
    private GraphService<Integer, EmptyEdgeProp> integerGraph;
    
    @BeforeEach
    public void setUp() {
        // Create an undirected graph for testing
        undirectedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.NO)
                .build();
        
        // Create a directed graph for testing
        directedGraph = new GraphBuilder<Character, EmptyEdgeProp>()
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

    // Tests for undirected graphs
    @Test
    public void testEmptyGraphHasNoCycle() {
        assertFalse(undirectedGraph.hasCycle(), "Empty graph should have no cycles");
        assertFalse(directedGraph.hasCycle(), "Empty directed graph should have no cycles");
    }

    @Test
    public void testSingleVertexHasNoCycle() {
        undirectedGraph.addVertex('A');
        assertFalse(undirectedGraph.hasCycle(), "Single vertex should have no cycles");
        
        directedGraph.addVertex('A');
        assertFalse(directedGraph.hasCycle(), "Single vertex in directed graph should have no cycles");
    }

    @Test
    public void testTwoVerticesNoEdgeHasNoCycle() {
        undirectedGraph.addVertex('A');
        undirectedGraph.addVertex('B');
        assertFalse(undirectedGraph.hasCycle(), "Two isolated vertices should have no cycles");
    }

    @Test
    public void testSingleEdgeHasNoCycle() {
        undirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        assertFalse(undirectedGraph.hasCycle(), "Single edge should have no cycles");
        
        directedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        assertFalse(directedGraph.hasCycle(), "Single directed edge should have no cycles");
    }

    @Test
    public void testTreeHasNoCycle() {
        // Create a tree structure
        undirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        undirectedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        undirectedGraph.addEdge('B', 'D', new EmptyEdgeProp());
        undirectedGraph.addEdge('B', 'E', new EmptyEdgeProp());
        undirectedGraph.addEdge('C', 'F', new EmptyEdgeProp());
        
        assertFalse(undirectedGraph.hasCycle(), "Tree should have no cycles");
    }

    @Test
    public void testUndirectedTriangleHasCycle() {
        // Create a triangle: A-B-C-A
        undirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        undirectedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        undirectedGraph.addEdge('C', 'A', new EmptyEdgeProp());
        
        assertTrue(undirectedGraph.hasCycle(), "Triangle should have a cycle");
    }

    @Test
    public void testUndirectedSquareHasCycle() {
        // Create a square: A-B-C-D-A
        undirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        undirectedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        undirectedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        undirectedGraph.addEdge('D', 'A', new EmptyEdgeProp());
        
        assertTrue(undirectedGraph.hasCycle(), "Square should have a cycle");
    }

    @Test
    public void testUndirectedStarHasNoCycle() {
        // Create a star graph: center connected to all others
        undirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        undirectedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        undirectedGraph.addEdge('A', 'D', new EmptyEdgeProp());
        undirectedGraph.addEdge('A', 'E', new EmptyEdgeProp());
        
        assertFalse(undirectedGraph.hasCycle(), "Star graph should have no cycles");
    }

    @Test
    public void testUndirectedDisconnectedWithCycle() {
        // Component 1: Tree (A-B)
        undirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        
        // Component 2: Triangle (C-D-E-C)
        undirectedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        undirectedGraph.addEdge('D', 'E', new EmptyEdgeProp());
        undirectedGraph.addEdge('E', 'C', new EmptyEdgeProp());
        
        assertTrue(undirectedGraph.hasCycle(), "Disconnected graph with one cycle should have cycles");
    }

    // Tests for directed graphs
    @Test
    public void testDirectedTriangleHasCycle() {
        // Create a directed triangle: A->B->C->A
        directedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        directedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        directedGraph.addEdge('C', 'A', new EmptyEdgeProp());
        
        assertTrue(directedGraph.hasCycle(), "Directed triangle should have a cycle");
    }

    @Test
    public void testDirectedDAGHasNoCycle() {
        // Create a Directed Acyclic Graph (DAG)
        // A->B, A->C, B->D, C->D
        directedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        directedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        directedGraph.addEdge('B', 'D', new EmptyEdgeProp());
        directedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        
        assertFalse(directedGraph.hasCycle(), "DAG should have no cycles");
    }

    @Test
    public void testDirectedSelfLoopHasCycle() {
        // Create a graph that accepts self-loops
        GraphService<Character, EmptyEdgeProp> selfLoopGraph = new GraphBuilder<Character, EmptyEdgeProp>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.NO)
                .build();
        
        selfLoopGraph.addEdge('A', 'A', new EmptyEdgeProp());
        
        assertTrue(selfLoopGraph.hasCycle(), "Self-loop should create a cycle");
    }

    @Test
    public void testDirectedComplexCycle() {
        // Create a more complex directed graph with cycle
        // A->B->C->D->B (cycle: B->C->D->B)
        directedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        directedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        directedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        directedGraph.addEdge('D', 'B', new EmptyEdgeProp());
        
        assertTrue(directedGraph.hasCycle(), "Complex directed graph with cycle should be detected");
    }

    @Test
    public void testDirectedMultipleComponents() {
        // Component 1: A->B (no cycle)
        directedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        
        // Component 2: C->D->E->C (cycle)
        directedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        directedGraph.addEdge('D', 'E', new EmptyEdgeProp());
        directedGraph.addEdge('E', 'C', new EmptyEdgeProp());
        
        assertTrue(directedGraph.hasCycle(), "Directed graph with one cyclic component should have cycles");
    }

    @Test
    public void testDirectedTreeHasNoCycle() {
        // Create a directed tree (all edges point away from root)
        directedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        directedGraph.addEdge('A', 'C', new EmptyEdgeProp());
        directedGraph.addEdge('B', 'D', new EmptyEdgeProp());
        directedGraph.addEdge('B', 'E', new EmptyEdgeProp());
        directedGraph.addEdge('C', 'F', new EmptyEdgeProp());
        
        assertFalse(directedGraph.hasCycle(), "Directed tree should have no cycles");
    }

    @Test
    public void testDirectedBackEdgeCreatesCycle() {
        // Create a path and then add a back edge
        directedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        directedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        directedGraph.addEdge('C', 'D', new EmptyEdgeProp());
        
        assertFalse(directedGraph.hasCycle(), "Path should have no cycles");
        
        // Add back edge to create cycle
        directedGraph.addEdge('D', 'A', new EmptyEdgeProp());
        
        assertTrue(directedGraph.hasCycle(), "Adding back edge should create a cycle");
    }

    @Test
    public void testIntegerVertices() {
        // Test with integer vertices to ensure generic type compatibility
        integerGraph.addEdge(1, 2, new EmptyEdgeProp());
        integerGraph.addEdge(2, 3, new EmptyEdgeProp());
        integerGraph.addEdge(3, 4, new EmptyEdgeProp());
        
        assertFalse(integerGraph.hasCycle(), "Path with integer vertices should have no cycles");
        
        // Add edge to create cycle
        integerGraph.addEdge(4, 1, new EmptyEdgeProp());
        
        assertTrue(integerGraph.hasCycle(), "Cycle with integer vertices should be detected");
    }

    @Test
    public void testLargeCycle() {
        // Create a large cycle with many vertices (A->B->C->...->Z->A)
        char[] vertices = new char[26];
        for (int i = 0; i < 26; i++) {
            vertices[i] = (char) ('A' + i);
        }
        
        // Add all edges in the cycle
        for (int i = 0; i < 25; i++) {
            undirectedGraph.addEdge(vertices[i], vertices[i + 1], new EmptyEdgeProp());
        }
        
        assertFalse(undirectedGraph.hasCycle(), "Path should have no cycles");
        
        // Close the cycle by connecting last to first
        undirectedGraph.addEdge(vertices[25], vertices[0], new EmptyEdgeProp()); // Z to A
        
        assertTrue(undirectedGraph.hasCycle(), "Large cycle should be detected");
    }

    @Test
    public void testComplexGraphStructure() {
        // Create a complex graph that mixes trees and cycles
        // Tree part: A-B-C
        undirectedGraph.addEdge('A', 'B', new EmptyEdgeProp());
        undirectedGraph.addEdge('B', 'C', new EmptyEdgeProp());
        
        // Additional tree branches
        undirectedGraph.addEdge('A', 'D', new EmptyEdgeProp());
        undirectedGraph.addEdge('D', 'E', new EmptyEdgeProp());
        
        assertFalse(undirectedGraph.hasCycle(), "Tree structure should have no cycles");
        
        // Add edge to create cycle
        undirectedGraph.addEdge('C', 'E', new EmptyEdgeProp());
        
        assertTrue(undirectedGraph.hasCycle(), "Adding edge to create cycle should be detected");
    }
} 