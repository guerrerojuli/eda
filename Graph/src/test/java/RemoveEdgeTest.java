import core.*;
import core.GraphService.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for removeEdge() methods
 * Tests both overloaded versions: removeEdge(V, V) and removeEdge(V, V, E)
 */
public class RemoveEdgeTest {

    @Test
    public void testRemoveAnyEdgeDirectedGraph() {
        // Test removeEdge(V, V) en grafo dirigido
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addEdge('A', 'B', new WeightedEdge(1));
        g.addEdge('A', 'B', new WeightedEdge(2));
        g.addEdge('A', 'C', new WeightedEdge(3));

        assertEquals(3, g.numberOfEdges(), "Debe haber 3 aristas inicialmente");

        // Remover cualquier arista entre A y B
        boolean result = g.removeEdge('A', 'B');
        
        assertTrue(result, "removeEdge debe retornar true cuando remueve exitosamente");
        assertEquals(2, g.numberOfEdges(), "Debe haber 2 aristas después de remover una");
    }

    @Test
    public void testRemoveSpecificEdgeDirectedGraph() {
        // Test removeEdge(V, V, E) en grafo dirigido
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        WeightedEdge edge1 = new WeightedEdge(1);
        WeightedEdge edge2 = new WeightedEdge(2);
        WeightedEdge edge3 = new WeightedEdge(3);

        g.addEdge('A', 'B', edge1);
        g.addEdge('A', 'B', edge2);
        g.addEdge('A', 'C', edge3);

        assertEquals(3, g.numberOfEdges(), "Debe haber 3 aristas inicialmente");

        // Remover arista específica entre A y B
        boolean result = g.removeEdge('A', 'B', edge1);
        
        assertTrue(result, "removeEdge debe retornar true cuando remueve exitosamente");
        assertEquals(2, g.numberOfEdges(), "Debe haber 2 aristas después de remover una específica");

        // Intentar remover la misma arista otra vez
        boolean result2 = g.removeEdge('A', 'B', edge1);
        assertFalse(result2, "removeEdge debe retornar false cuando la arista ya no existe");
        assertEquals(2, g.numberOfEdges(), "El número de aristas no debe cambiar");
    }

    @Test
    public void testRemoveEdgeUndirectedGraph() {
        // Test removeEdge en grafo no dirigido
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        WeightedEdge edge1 = new WeightedEdge(1);
        g.addEdge('A', 'B', edge1);
        g.addEdge('B', 'C', new WeightedEdge(2));

        assertEquals(2, g.numberOfEdges(), "Debe haber 2 aristas inicialmente");

        // Remover arista específica entre A y B (debe remover en ambas direcciones)
        boolean result = g.removeEdge('A', 'B', edge1);
        
        assertTrue(result, "removeEdge debe retornar true cuando remueve exitosamente");
        assertEquals(1, g.numberOfEdges(), "Debe haber 1 arista después de remover una");

        // Verificar que no se puede remover desde B a A (ya fue removida)
        boolean result2 = g.removeEdge('B', 'A', edge1);
        assertFalse(result2, "removeEdge debe retornar false cuando la arista ya no existe");
    }

    @Test
    public void testRemoveSelfLoop() {
        // Test remover self-loop
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.UNDIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        WeightedEdge selfLoopEdge = new WeightedEdge(5);
        g.addEdge('A', 'A', selfLoopEdge);
        g.addEdge('A', 'B', new WeightedEdge(1));

        assertEquals(2, g.numberOfEdges(), "Debe haber 2 aristas inicialmente");

        // Remover self-loop
        boolean result = g.removeEdge('A', 'A', selfLoopEdge);
        
        assertTrue(result, "removeEdge debe retornar true cuando remueve self-loop exitosamente");
        assertEquals(1, g.numberOfEdges(), "Debe haber 1 arista después de remover self-loop");
    }

    @Test
    public void testRemoveNonExistentEdge() {
        // Test remover arista que no existe
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addEdge('A', 'B', new WeightedEdge(1));

        // Intentar remover arista que no existe
        boolean result1 = g.removeEdge('B', 'A'); // Dirección opuesta
        assertFalse(result1, "removeEdge debe retornar false cuando la arista no existe");

        boolean result2 = g.removeEdge('A', 'C'); // Vértice no existe
        assertFalse(result2, "removeEdge debe retornar false cuando el vértice no existe");

        WeightedEdge nonExistentEdge = new WeightedEdge(999);
        boolean result3 = g.removeEdge('A', 'B', nonExistentEdge);
        assertFalse(result3, "removeEdge debe retornar false cuando la arista específica no existe");

        assertEquals(1, g.numberOfEdges(), "El número de aristas no debe cambiar");
    }

    @Test
    public void testRemoveEdgeWithNullParameters() {
        // Test con parámetros null
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addEdge('A', 'B', new WeightedEdge(1));

        // Test con parámetros null
        assertFalse(g.removeEdge(null, 'B'), "removeEdge debe retornar false con aVertex null");
        assertFalse(g.removeEdge('A', null), "removeEdge debe retornar false con otherVertex null");
        assertFalse(g.removeEdge(null, null), "removeEdge debe retornar false con ambos vértices null");
        
        assertFalse(g.removeEdge(null, 'B', new WeightedEdge(1)), "removeEdge debe retornar false con aVertex null");
        assertFalse(g.removeEdge('A', null, new WeightedEdge(1)), "removeEdge debe retornar false con otherVertex null");
        assertFalse(g.removeEdge('A', 'B', null), "removeEdge debe retornar false con theEdge null");

        assertEquals(1, g.numberOfEdges(), "El número de aristas no debe cambiar");
    }

    @Test
    public void testRemoveMultipleEdgesBetweenSameVertices() {
        // Test remover múltiples aristas entre los mismos vértices
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        WeightedEdge edge1 = new WeightedEdge(1);
        WeightedEdge edge2 = new WeightedEdge(2);
        WeightedEdge edge3 = new WeightedEdge(3);

        g.addEdge('A', 'B', edge1);
        g.addEdge('A', 'B', edge2);
        g.addEdge('A', 'B', edge3);

        assertEquals(3, g.numberOfEdges(), "Debe haber 3 aristas inicialmente");

        // Remover aristas específicas una por una
        assertTrue(g.removeEdge('A', 'B', edge2), "Debe remover edge2 exitosamente");
        assertEquals(2, g.numberOfEdges(), "Debe haber 2 aristas después de remover edge2");

        assertTrue(g.removeEdge('A', 'B', edge1), "Debe remover edge1 exitosamente");
        assertEquals(1, g.numberOfEdges(), "Debe haber 1 arista después de remover edge1");

        assertTrue(g.removeEdge('A', 'B', edge3), "Debe remover edge3 exitosamente");
        assertEquals(0, g.numberOfEdges(), "No debe haber aristas después de remover todas");
    }

    @Test
    public void testRemoveAnyEdgeMultipleEdges() {
        // Test removeEdge(V, V) cuando hay múltiples aristas
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addEdge('A', 'B', new WeightedEdge(1));
        g.addEdge('A', 'B', new WeightedEdge(2));
        g.addEdge('A', 'B', new WeightedEdge(3));

        assertEquals(3, g.numberOfEdges(), "Debe haber 3 aristas inicialmente");

        // Remover cualquier arista entre A y B (debe remover solo una)
        assertTrue(g.removeEdge('A', 'B'), "Debe remover una arista exitosamente");
        assertEquals(2, g.numberOfEdges(), "Debe haber 2 aristas después de remover una");

        assertTrue(g.removeEdge('A', 'B'), "Debe remover otra arista exitosamente");
        assertEquals(1, g.numberOfEdges(), "Debe haber 1 arista después de remover otra");

        assertTrue(g.removeEdge('A', 'B'), "Debe remover la última arista exitosamente");
        assertEquals(0, g.numberOfEdges(), "No debe haber aristas después de remover todas");

        assertFalse(g.removeEdge('A', 'B'), "No debe poder remover más aristas");
    }
} 