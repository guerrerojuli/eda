import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

public class KMPTest {
    
    @Test
    public void testBasicPatternMatching() {
        String target = "ABABDABACDABABCABAB";
        String query = "ABABCABAB";
        assertEquals(10, KMP.indexOf(query.toCharArray(), target.toCharArray()));
    }
    
    @Test
    public void testPatternNotFound() {
        String target = "ABABDABACDABABCABAB";
        String query = "XYZ";
        assertEquals(-1, KMP.indexOf(query.toCharArray(), target.toCharArray()));
    }
    
    @Test
    public void testEmptyPattern() {
        String target = "ABABDABACDABABCABAB";
        String query = "";
        assertEquals(0, KMP.indexOf(query.toCharArray(), target.toCharArray()));
    }
    
    @Test
    public void testEmptyText() {
        String target = "";
        String query = "ABC";
        assertEquals(-1, KMP.indexOf(query.toCharArray(), target.toCharArray()));
    }
    
    @Test
    public void testPatternAtStart() {
        String target = "ABCABABDABACDABABCABAB";
        String query = "ABC";
        assertEquals(0, KMP.indexOf(query.toCharArray(), target.toCharArray()));
    }
    
    @Test
    public void testPatternAtEnd() {
        String target = "ABABDABACDABABCABAB";
        String query = "ABAB";
        assertEquals(0, KMP.indexOf(query.toCharArray(), target.toCharArray()));
    }
    
    @Test
    public void testPatternLongerThanText() {
        String target = "ABC";
        String query = "ABCDEF";
        assertEquals(-1, KMP.indexOf(query.toCharArray(), target.toCharArray()));
    }
    
    @Test
    public void testRepeatedPattern() {
        String target = "AAAAA";
        String query = "AA";
        assertEquals(0, KMP.indexOf(query.toCharArray(), target.toCharArray()));
    }

    @Test
    public void testAllOccurrences() {
        char[] query1 = "no".toCharArray();
        char[] target1= "sino se los digo no se si es nocivo".toCharArray();
        assertEquals(Arrays.asList(2, 17, 29), KMP.findAll(query1, target1));

        char[] query2= "ni".toCharArray();
        char[] target2= "sino se los digo no se si es nocivo".toCharArray();
        assertEquals(Arrays.asList(), KMP.findAll(query2, target2));

        char[] query3 = "aaa".toCharArray();
        char[] target3 = "aaabaaaaab".toCharArray();
        assertEquals(Arrays.asList(0, 4, 5, 6), KMP.findAll(query3, target3));
    }

    @Test
    public void testEj5() {
        int[] target = {10, 5, 3, 5, 3, 5, 3, 5, 5, 5, 3, 5, 3};

        // Test case 1: Pattern 5,3,5,3
        int[] query1 = {5, 3, 5, 3};
        assertEquals(Arrays.asList(1, 3, 9), KMP.findAll(query1, target));

        // Test case 2: Pattern 5,5,5
        int[] query2 = {5, 5, 5};
        assertEquals(Arrays.asList( 7), KMP.findAll(query2, target));

        // Test case 3: Pattern 5,3,8
        int[] query3 = {5, 3, 8};
        assertEquals(Arrays.asList(), KMP.findAll(query3, target));

    }
    
} 