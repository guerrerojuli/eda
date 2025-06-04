import core.*;
import core.GraphService.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for removeVertex() method
 * Tests different scenarios including vertices with edges and isolated vertices
 */
public class RemoveVertexTest {

    @Test
    public void testCasoDeUsoA_RemoveVertexWithIncomingAndOutgoingEdges() {
        // Caso de Uso A: Remover vértice G que tiene aristas entrantes y salientes
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

        // Verificar estado inicial
        assertTrue(g.getVertices().contains('G'), "G debe existir antes de removerlo");
        assertEquals(4, g.numberOfVertices(), "Debe haber 4 vértices inicialmente (D, G, F, U)");
        assertEquals(5, g.numberOfEdges(), "Debe haber 5 aristas inicialmente");

        // Remover vértice G
        boolean result = g.removeVertex('G');
        
        assertTrue(result, "removeVertex debe retornar true cuando remueve exitosamente");
        assertFalse(g.getVertices().contains('G'), "G no debe existir después de removerlo");
        assertEquals(3, g.numberOfVertices(), "Debe haber 3 vértices después de remover G");
        assertEquals(2, g.numberOfEdges(), "Debe haber 2 aristas después de remover G (solo los self-loops de F)");
    }

    @Test
    public void testCasoDeUsoB_RemoveIsolatedVertex() {
        // Caso de Uso B: Remover vértice D que no tiene aristas
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addVertex('D');
        g.addVertex('U');
        g.addEdge('F', 'F', new WeightedEdge(3));
        g.addEdge('F', 'F', new WeightedEdge(2));

        // Verificar estado inicial
        assertTrue(g.getVertices().contains('D'), "D debe existir antes de removerlo");
        assertEquals(3, g.numberOfVertices(), "Debe haber 3 vértices inicialmente (D, U, F)");
        assertEquals(2, g.numberOfEdges(), "Debe haber 2 aristas inicialmente");

        // Remover vértice D
        boolean result = g.removeVertex('D');
        
        assertTrue(result, "removeVertex debe retornar true cuando remueve exitosamente");
        assertFalse(g.getVertices().contains('D'), "D no debe existir después de removerlo");
        assertEquals(2, g.numberOfVertices(), "Debe haber 2 vértices después de remover D");
        assertEquals(2, g.numberOfEdges(), "Debe haber 2 aristas después de remover D (sin cambio)");
    }

    @Test
    public void testCasoDeUsoC_RemoveVertexFromUndirectedGraph() {
        // Caso de Uso C: Remover vértice G en grafo no dirigido
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.UNDIRECTED)
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

        // Verificar estado inicial
        assertTrue(g.getVertices().contains('G'), "G debe existir antes de removerlo");
        assertEquals(4, g.numberOfVertices(), "Debe haber 4 vértices inicialmente");
        assertEquals(5, g.numberOfEdges(), "Debe haber 5 aristas inicialmente (3 conectadas a G + 2 self-loops de F)");

        // Remover vértice G
        boolean result = g.removeVertex('G');
        
        assertTrue(result, "removeVertex debe retornar true cuando remueve exitosamente");
        assertFalse(g.getVertices().contains('G'), "G no debe existir después de removerlo");
        assertEquals(3, g.numberOfVertices(), "Debe haber 3 vértices después de remover G");
        assertEquals(2, g.numberOfEdges(), "Debe haber 2 aristas después de remover G (solo los self-loops de F)");
    }

    @Test
    public void testRemoveNonExistentVertex() {
        // Test adicional: remover vértice que no existe
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addVertex('A');
        
        boolean result = g.removeVertex('X');
        assertFalse(result, "removeVertex debe retornar false cuando el vértice no existe");
        assertEquals(1, g.numberOfVertices(), "El número de vértices no debe cambiar");
    }

    @Test
    public void testRemoveNullVertex() {
        // Test adicional: remover vértice null
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.SIMPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.NO)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addVertex('A');
        
        boolean result = g.removeVertex(null);
        assertFalse(result, "removeVertex debe retornar false cuando el vértice es null");
        assertEquals(1, g.numberOfVertices(), "El número de vértices no debe cambiar");
    }

    @Test
    public void testRemoveVertexWithSelfLoops() {
        // Test adicional: remover vértice que tiene self-loops
        GraphService<Character, WeightedEdge> g = new GraphBuilder<Character, WeightedEdge>()
                .withMultiplicity(Multiplicity.MULTIPLE)
                .withDirected(EdgeMode.DIRECTED)
                .withAcceptSelfLoop(SelfLoop.YES)
                .withAcceptWeight(Weight.YES)
                .build();

        g.addEdge('A', 'A', new WeightedEdge(1)); // Self-loop
        g.addEdge('A', 'A', new WeightedEdge(2)); // Another self-loop
        g.addEdge('A', 'B', new WeightedEdge(3)); // Edge to B

        assertEquals(2, g.numberOfVertices(), "Debe haber 2 vértices inicialmente");
        assertEquals(3, g.numberOfEdges(), "Debe haber 3 aristas inicialmente");

        boolean result = g.removeVertex('A');
        
        assertTrue(result, "removeVertex debe retornar true");
        assertFalse(g.getVertices().contains('A'), "A no debe existir después de removerlo");
        assertEquals(1, g.numberOfVertices(), "Debe quedar 1 vértice (B)");
        assertEquals(0, g.numberOfEdges(), "No deben quedar aristas");
    }
} 