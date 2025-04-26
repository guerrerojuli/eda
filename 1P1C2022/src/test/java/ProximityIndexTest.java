import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProximityIndexTest {
    @Test
    public void test() {
        ProximityIndex index = new ProximityIndex();
        index.initialize(new String[]{"Ana", "Carlos", "Juan", "Yolanda"});

        // Carlos - 2
        assertEquals("Yolanda", index.search("Carlos", 2));
        // Carlos - 3
        assertEquals("Ana", index.search("Carlos", 3));
        // Ana - 14
        assertEquals("Juan", index.search("Ana", 14));
        // Ana - -2
        assertEquals("Juan", index.search("Ana", -2));
        // Ana - -17
        assertEquals("Yolanda", index.search("Ana", -17));
        // Juan - -4
        assertEquals("Juan", index.search("Juan", -4));
        // XXX - -4
        assertNull(index.search("XXX", -4));
    }
}
