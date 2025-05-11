import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BagTest {
    private Bag<String> bag;

    @BeforeEach
    void setUp() {
        bag = new HashBag<>();
    }

    @Test
    void testAdd() {
        // Test adding single element
        assertTrue(bag.add("apple"));
        assertEquals(1, bag.getCount("apple"));

        // Test adding multiple elements
        bag.add("banana", 3);
        assertEquals(3, bag.getCount("banana"));

        // Test adding to existing element
        bag.add("apple");
        assertEquals(2, bag.getCount("apple"));
    }

    @Test
    void testRemove() {
        // Setup
        bag.add("apple", 3);
        bag.add("banana", 2);

        // Test removing one occurrence
        assertTrue(bag.remove("apple", 1));
        assertEquals(2, bag.getCount("apple"));

        // Test removing multiple occurrences
        assertTrue(bag.remove("banana", 2));
        assertEquals(0, bag.getCount("banana"));

        // Test removing non-existent element
        assertFalse(bag.remove("orange"));

        // Test removing more occurrences than exist
        bag.add("apple", 2);
        assertTrue(bag.remove("apple"));
        assertEquals(0, bag.getCount("apple"));
    }

    @Test
    void testGetCount() {
        // Test count of non-existent element
        assertEquals(0, bag.getCount("apple"));

        // Test count after adding elements
        bag.add("apple", 3);
        assertEquals(3, bag.getCount("apple"));

        // Test count after removing elements
        bag.remove("apple", 2);
        assertEquals(1, bag.getCount("apple"));
    }

    @Test
    void testMultipleOperations() {
        // Test a sequence of operations
        bag.add("apple", 2);
        bag.add("banana", 3);
        bag.add("orange", 1);

        assertEquals(2, bag.getCount("apple"));
        assertEquals(3, bag.getCount("banana"));
        assertEquals(1, bag.getCount("orange"));

        bag.remove("banana", 2);
        assertEquals(1, bag.getCount("banana"));

        bag.add("apple");
        assertEquals(3, bag.getCount("apple"));

        assertTrue(bag.remove("orange"));
        assertEquals(0, bag.getCount("orange"));
    }

    @Test
    void testWithPrimitiveInteger() {
        MyBag<Integer> myBag = new MyBag<>(i -> i.hashCode());
        
        myBag.add(10);
        myBag.add(20);
        myBag.add(10);
        myBag.add(10);
        
        assertEquals(0, myBag.getCount(5));
        assertEquals(3, myBag.getCount(10));
    }
    
    @Test
    void testWithMyInteger() {
        MyBag<MyInteger> myBag = new MyBag<>(MyInteger::hashCode);
        
        myBag.add(new MyInteger(10));
        myBag.add(new MyInteger(20));
        myBag.add(new MyInteger(10));
        myBag.add(new MyInteger(10));
        
        assertEquals(0, myBag.getCount(new MyInteger(5)));
        assertEquals(3, myBag.getCount(new MyInteger(10)));
    }

    @Test
    void testRemoveWithMyInteger() {
        MyBag<MyInteger> myBag = new MyBag<>(MyInteger::hashCode);
        
        myBag.add(new MyInteger(10));
        myBag.add(new MyInteger(10));
        myBag.add(new MyInteger(10));
        
        assertEquals(3, myBag.getCount(new MyInteger(10)));
        
        myBag.remove(new MyInteger(10));
        assertEquals(2, myBag.getCount(new MyInteger(10)));
        
        myBag.remove(new MyInteger(10));
        assertEquals(1, myBag.getCount(new MyInteger(10)));
        
        myBag.remove(new MyInteger(10));
        assertEquals(0, myBag.getCount(new MyInteger(10)));
    }
} 