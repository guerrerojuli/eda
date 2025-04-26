import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IndexWithDuplicatesTest {
  private IndexWithDuplicates<Integer> index;
  private int[] array;
  private int[] expected;

  @BeforeEach
  public void setUp() {
    index = new IndexWithDuplicates<>(Integer.class);
    array = new int[] { 100, 50, 30, 50, 80, 100, 100, 30 };
    expected = new int[] { 30, 30, 50, 50, 80, 100, 100, 100 };
  }

  @Test
  public void testIndexWithDuplicates() {
    assertThrows(IllegalArgumentException.class, () -> index.initialize(null));
    assertEquals(0, index.occurrences(10));

    index.delete(10);
    assertFalse(index.search(10));

    index.insert(80);
    index.insert(20);
    index.insert(80);

    try {
      index.initialize(new Integer[] { 100, 50, 30, 50, 80, 100, 100, 30 });
    } catch (Exception e) {
      fail("Exception thrown");
    }
    assertFalse(index.search(20));
    assertTrue(index.search(80));
    assertEquals(2, index.occurrences(50));
    index.delete(50);
    assertEquals(1, index.occurrences(50));

    try {
      index.initialize(new Integer[] { 100, 50, 30, 50, 80, 100, 100, 30 }); // guarda 30 30 50 50 80 100 100 100
    } catch (Exception e) {
      fail("Exception thrown");
    }
    index.sortedPrint();
    Integer[] rta = index.range(50, 100, false, false); // [80]
    assertArrayEquals(new Integer[] { 80 }, rta);

    rta = index.range(30, 50, true, false); // [30, 30]
    assertArrayEquals(new Integer[] { 30, 30 }, rta);

    rta = index.range(45, 100, false, false); // [50, 50, 80]
    assertArrayEquals(new Integer[] { 50, 50, 80 }, rta);

    rta = index.range(45, 100, true, false); // [50, 50, 80]
    assertArrayEquals(new Integer[] { 50, 50, 80 }, rta);

    rta = index.range(10, 50, true, false); // [30, 30]
    assertArrayEquals(new Integer[] { 30, 30 }, rta);

    rta = index.range(10, 20, false, false); // []
    assertArrayEquals(new Integer[] {}, rta);
  }

  @Test
  public void testMergesort() {
    Mergesort.sort(array);
    assertArrayEquals(expected, array);
  }

  @Test
  public void testQuicksort() {
    Quicksort.sort(array);
    assertArrayEquals(expected, array);
  }
}
