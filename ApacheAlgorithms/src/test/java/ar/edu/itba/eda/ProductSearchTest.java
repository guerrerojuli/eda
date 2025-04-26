package ar.edu.itba.eda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductSearchTest {
    private ProductSearch productSearch;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary file with test products
        tempFile = Files.createTempFile("test_products", ".txt");
        Files.write(tempFile, List.of(
            "porta rollo",
            "seca platos",
            "tenderero",
            "porta royo",
            "secaplatos",
            "tender"
        ));
        productSearch = new ProductSearch(tempFile.toString());
    }

    @Test
    void testSearchVariations() throws IOException {
        // Test case 1: "portarollo"
        List<ProductSearch.SearchResult> results1 = productSearch.findSimilarProducts("portarollo", 5);
        assertFalse(results1.isEmpty());
        assertEquals("porta rollo", results1.get(0).getProduct().getName());

        // Test case 2: "secaplatos"
        List<ProductSearch.SearchResult> results2 = productSearch.findSimilarProducts("secaplatos", 5);
        assertFalse(results2.isEmpty());
        assertTrue(results2.get(0).getProduct().getName().equals("secaplatos") || 
                  results2.get(0).getProduct().getName().equals("seca platos"));

        // Test case 3: "tenderero"
        List<ProductSearch.SearchResult> results3 = productSearch.findSimilarProducts("tenderero", 5);
        assertFalse(results3.isEmpty());
        assertEquals("tenderero", results3.get(0).getProduct().getName());

        // Test case 4: "tender"
        List<ProductSearch.SearchResult> results4 = productSearch.findSimilarProducts("tender", 5);
        assertFalse(results4.isEmpty());
        assertEquals("tender", results4.get(0).getProduct().getName());

        // Test case 5: "ténder"
        List<ProductSearch.SearchResult> results5 = productSearch.findSimilarProducts("ténder", 5);
        assertFalse(results5.isEmpty());
        assertEquals("tender", results5.get(0).getProduct().getName());

        // Test case 6: "porta royo"
        List<ProductSearch.SearchResult> results6 = productSearch.findSimilarProducts("porta royo", 5);
        assertFalse(results6.isEmpty());
        assertEquals("porta royo", results6.get(0).getProduct().getName());

        // Test case 7: "seca plato"
        List<ProductSearch.SearchResult> results7 = productSearch.findSimilarProducts("seca plato", 5);
        assertFalse(results7.isEmpty());
        assertTrue(results7.get(0).getProduct().getName().equals("secaplatos") || 
                  results7.get(0).getProduct().getName().equals("seca platos"));
    }

    @Test
    void testNormalization() throws IOException {
        // Test with different cases and accents
        List<ProductSearch.SearchResult> results = productSearch.findSimilarProducts("Pórta Róllo", 5);
        assertFalse(results.isEmpty());
        assertEquals("porta rollo", results.get(0).getProduct().getName());
    }

    @Test
    void testMultipleSpaces() throws IOException {
        // Test with multiple spaces
        List<ProductSearch.SearchResult> results = productSearch.findSimilarProducts("porta   rollo", 5);
        assertFalse(results.isEmpty());
        assertEquals("porta rollo", results.get(0).getProduct().getName());
    }
} 