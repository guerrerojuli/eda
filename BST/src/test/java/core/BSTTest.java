package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BSTTest {
    private BST<Integer> bst;
    private BST<Integer> bstWithDuplicates;
    private BST<Integer> bstNoRepeats;

    @BeforeEach
    void setUp() {
        bst = new BST<>();
        bstWithDuplicates = new BST<>();
        bstNoRepeats = new BST<>();

        // Setup for bstWithDuplicates (also used for getCommonNodeWithRepeated)
        // In-order: 10, 20, 30, 30, 35, 40, 50, 60, 70, 70, 70, 80
        bstWithDuplicates.insert(50); // root
        bstWithDuplicates.insert(30);
        bstWithDuplicates.insert(70);
        bstWithDuplicates.insert(20);
        bstWithDuplicates.insert(40);
        bstWithDuplicates.insert(60);
        bstWithDuplicates.insert(80);
        bstWithDuplicates.insert(10);
        bstWithDuplicates.insert(35);
        bstWithDuplicates.insert(70); // Duplicate 70 (2nd)
        bstWithDuplicates.insert(30); // Duplicate 30 (2nd)
        bstWithDuplicates.insert(70); // Duplicate 70 (3rd)
        

        // Setup for bstNoRepeats (a standard BST without duplicates for getCommonNode)
        bstNoRepeats.insert(50); bstNoRepeats.insert(30); bstNoRepeats.insert(70);
        bstNoRepeats.insert(20); bstNoRepeats.insert(40); bstNoRepeats.insert(60);
        bstNoRepeats.insert(80); bstNoRepeats.insert(10); bstNoRepeats.insert(75);
        bstNoRepeats.insert(90);
    }

    @Test
    void testGetOcurrencesEmptyTree() {
        assertEquals(0, bst.getOcurrences(5));
    }

    @Test
    void testGetOcurrencesNoRepetitions() {
        bst.insert(5);
        bst.insert(3);
        bst.insert(7);
        
        assertEquals(1, bst.getOcurrences(5));
        assertEquals(1, bst.getOcurrences(3));
        assertEquals(1, bst.getOcurrences(7));
        assertEquals(0, bst.getOcurrences(4));
    }

    @Test
    void testGetOcurrencesWithRepetitions() {
        BST<Integer> localBst = new BST<>();
        localBst.insert(5);
        localBst.insert(5);
        localBst.insert(5);
        localBst.insert(3);
        localBst.insert(7);
        localBst.insert(7);
        
        assertEquals(3, localBst.getOcurrences(5));
        assertEquals(1, localBst.getOcurrences(3));
        assertEquals(2, localBst.getOcurrences(7));
    }

    @Test
    void testGetOcurrencesWithNullElement() {
        bst.insert(5);
        assertEquals(0, bst.getOcurrences(null));
    }

    @Test
    void testGetOcurrencesInRightSubtree() {
        BST<Integer> localBst = new BST<>();
        localBst.insert(5);
        localBst.insert(7);
        localBst.insert(7);
        localBst.insert(7);
        localBst.insert(6);
        localBst.insert(8);
        
        assertEquals(3, localBst.getOcurrences(7));
    }

    @Test
    void testGetOcurrencesWithMixedValues() {
        BST<Integer> localBst = new BST<>();
        localBst.insert(0);
        localBst.insert(-5);
        localBst.insert(-5);
        localBst.insert(5);
        localBst.insert(5);
        localBst.insert(5);
        
        assertEquals(2, localBst.getOcurrences(-5));
        assertEquals(3, localBst.getOcurrences(5));
        assertEquals(1, localBst.getOcurrences(0));
    }

    @Test
    void testInsertAndContains() {
        bst.insert(5);
        assertTrue(bst.contains(5));
        assertEquals(1, bst.getOcurrences(5));
        
        bst.insert(5);
        assertTrue(bst.contains(5));
        assertEquals(2, bst.getOcurrences(5));
        
        assertFalse(bst.contains(3));
        assertEquals(0, bst.getOcurrences(3));
    }

    @Test
    void testComplexTreeStructure() {
        BST<Integer> localBst = new BST<>();
        localBst.insert(10);
        localBst.insert(5);
        localBst.insert(15);
        localBst.insert(5); 
        localBst.insert(15); 
        localBst.insert(3);
        localBst.insert(7);
        localBst.insert(13);
        localBst.insert(17);
        localBst.insert(15); 
        
        assertEquals(2, localBst.getOcurrences(5));
        assertEquals(3, localBst.getOcurrences(15));
        assertEquals(1, localBst.getOcurrences(10));
        assertEquals(1, localBst.getOcurrences(3));
        assertEquals(0, localBst.getOcurrences(100));
    }

    // Tests for Kesimo method
    @Test
    void testKesimoEmptyTree() {
        assertNull(bst.Kesimo(1));
    }

    @Test
    void testKesimoInvalidK() {
        bst.insert(10);
        assertNull(bst.Kesimo(0));
        assertNull(bst.Kesimo(-1));
    }

    @Test
    void testKesimoSingleNodeTree() {
        bst.insert(10);
        assertEquals(10, bst.Kesimo(1));
        assertNull(bst.Kesimo(2));
    }

    @Test
    void testKesimoSimpleTree() {
        bst.insert(20);
        bst.insert(10);
        bst.insert(30);
        assertEquals(10, bst.Kesimo(1));
        assertEquals(20, bst.Kesimo(2));
        assertEquals(30, bst.Kesimo(3));
        assertNull(bst.Kesimo(4));
    }

    @Test
    void testKesimoWithDuplicatesFromDescription() {
        // bstWithDuplicates in-order: 10, 20, 30, 30, 35, 40, 50, 60, 70, 70, 70, 80
        assertEquals(10, bstWithDuplicates.Kesimo(1));
        assertEquals(20, bstWithDuplicates.Kesimo(2));
        assertEquals(30, bstWithDuplicates.Kesimo(3)); 
        assertEquals(30, bstWithDuplicates.Kesimo(4)); 
        assertEquals(35, bstWithDuplicates.Kesimo(5)); 
        assertEquals(40, bstWithDuplicates.Kesimo(6)); 
        assertEquals(50, bstWithDuplicates.Kesimo(7)); 
        assertEquals(60, bstWithDuplicates.Kesimo(8)); 
        assertEquals(70, bstWithDuplicates.Kesimo(9)); 
        assertEquals(70, bstWithDuplicates.Kesimo(10));
        assertEquals(70, bstWithDuplicates.Kesimo(11));
        assertEquals(80, bstWithDuplicates.Kesimo(12));
        assertNull(bstWithDuplicates.Kesimo(13));
    }

    @Test
    void testKesimoLeftSkewedTree() {
        bst.insert(30);
        bst.insert(20);
        bst.insert(10);
        assertEquals(10, bst.Kesimo(1));
        assertEquals(20, bst.Kesimo(2));
        assertEquals(30, bst.Kesimo(3));
        assertNull(bst.Kesimo(4));
    }

    @Test
    void testKesimoRightSkewedTree() {
        bst.insert(10);
        bst.insert(20);
        bst.insert(30);
        assertEquals(10, bst.Kesimo(1));
        assertEquals(20, bst.Kesimo(2));
        assertEquals(30, bst.Kesimo(3));
        assertNull(bst.Kesimo(4));
    }

    @Test
    void testKesimoComplexStructureWithDuplicates() {
        BST<Integer> localBst = new BST<>(); 
        localBst.insert(50); localBst.insert(30); localBst.insert(70);
        localBst.insert(20); localBst.insert(40); localBst.insert(60);
        localBst.insert(80); localBst.insert(30); localBst.insert(70);
        localBst.insert(70);
        assertEquals(20, localBst.Kesimo(1));
        assertEquals(30, localBst.Kesimo(2));
        assertEquals(30, localBst.Kesimo(3)); 
        assertEquals(40, localBst.Kesimo(4)); 
        assertEquals(50, localBst.Kesimo(5)); 
        assertEquals(60, localBst.Kesimo(6)); 
        assertEquals(70, localBst.Kesimo(7)); 
        assertEquals(70, localBst.Kesimo(8)); 
        assertEquals(70, localBst.Kesimo(9)); 
        assertEquals(80, localBst.Kesimo(10));
        assertNull(localBst.Kesimo(11));
    }

    // Tests for getCommonNode method (assumes no repeats)
    @Test
    void testGetCommonNodeEmptyTree() {
        BST<Integer> emptyBst = new BST<>();
        assertNull(emptyBst.getCommonNode(10,20));
    }

    @Test
    void testGetCommonNodeNullElements() {
        assertNull(bstNoRepeats.getCommonNode(null, 20));
        assertNull(bstNoRepeats.getCommonNode(10, null));
        assertNull(bstNoRepeats.getCommonNode(null, null));
    }

    @Test
    void testGetCommonNodeEqualElements() {
        assertNull(bstNoRepeats.getCommonNode(20, 20));
    }

    @Test
    void testGetCommonNodeOneElementNotPresent() {
        assertNull(bstNoRepeats.getCommonNode(10, 100)); 
        assertNull(bstNoRepeats.getCommonNode(100, 10)); 
        bstNoRepeats.insert(35); 
        assertNull(bstNoRepeats.getCommonNode(35, 100)); 
    }
    
    @Test
    void testGetCommonNodeBothElementsNotPresent() {
        assertNull(bstNoRepeats.getCommonNode(100, 200)); 
    }

    @Test
    void testGetCommonNodeBasic() {
        assertEquals(30, bstNoRepeats.getCommonNode(20, 40));
        assertEquals(70, bstNoRepeats.getCommonNode(60, 80));
        assertEquals(30, bstNoRepeats.getCommonNode(10, 40));
        assertEquals(50, bstNoRepeats.getCommonNode(10, 90));
    }

    @Test
    void testGetCommonNodeOneIsAncestor() {
        assertEquals(50, bstNoRepeats.getCommonNode(50, 20));
        assertEquals(30, bstNoRepeats.getCommonNode(30, 10));
        assertEquals(70, bstNoRepeats.getCommonNode(70, 90));
        assertEquals(50, bstNoRepeats.getCommonNode(20, 50));
    }

    @Test
    void testGetCommonNodeDifferentBranches() {
        assertEquals(50, bstNoRepeats.getCommonNode(20, 60));
        assertEquals(50, bstNoRepeats.getCommonNode(40, 75));
    }

    @Test
    void testGetCommonNodeDeepNodes() {
        assertEquals(20, bstNoRepeats.getCommonNode(10, 20)); 
        assertEquals(80, bstNoRepeats.getCommonNode(75, 90));
    }

    // Tests for getCommonNodeWithRepeated method
    @Test
    void testGetCommonNodeWithRepeatedEmptyTree() {
        BST<Integer> emptyBst = new BST<>();
        assertNull(emptyBst.getCommonNodeWithRepeated(10, 20));
    }

    @Test
    void testGetCommonNodeWithRepeatedNullElements() {
        assertNull(bstWithDuplicates.getCommonNodeWithRepeated(null, 20));
        assertNull(bstWithDuplicates.getCommonNodeWithRepeated(10, null));
        assertNull(bstWithDuplicates.getCommonNodeWithRepeated(null, null));
    }

    @Test
    void testGetCommonNodeWithRepeated_EqualElements_NotEnoughOccurrences() {
        BST<Integer> localBst = new BST<>();
        localBst.insert(10);
        assertNull(localBst.getCommonNodeWithRepeated(10, 10)); // Only one 10
    }

    @Test
    void testGetCommonNodeWithRepeated_EqualElements_EnoughOccurrences() {
        // bstWithDuplicates has 70 (x3) and 30 (x2)
        assertEquals(70, bstWithDuplicates.getCommonNodeWithRepeated(70, 70));
        assertEquals(30, bstWithDuplicates.getCommonNodeWithRepeated(30, 30));
    }

    @Test
    void testGetCommonNodeWithRepeated_DistinctElements_OneNotPresent() {
        assertNull(bstWithDuplicates.getCommonNodeWithRepeated(10, 100)); // 100 not present
        assertNull(bstWithDuplicates.getCommonNodeWithRepeated(100, 10)); // 100 not present
    }

    @Test
    void testGetCommonNodeWithRepeated_DistinctElements_BothNotPresent() {
        assertNull(bstWithDuplicates.getCommonNodeWithRepeated(100, 200));
    }

    @Test
    void testGetCommonNodeWithRepeated_DistinctElements_Basic() {
        // bstWithDuplicates: root 50. In-order: 10, 20, 30, 30, 35, 40, 50, 60, 70, 70, 70, 80
        assertEquals(30, bstWithDuplicates.getCommonNodeWithRepeated(20, 40)); // LCA of 20 and 40 is 30
        assertEquals(70, bstWithDuplicates.getCommonNodeWithRepeated(60, 80)); // LCA of 60 and 80 is 70
        assertEquals(30, bstWithDuplicates.getCommonNodeWithRepeated(10, 35)); // LCA of 10 and 35 is 30
        assertEquals(50, bstWithDuplicates.getCommonNodeWithRepeated(10, 80)); // LCA of 10 and 80 is 50 (root)
        assertEquals(70, bstWithDuplicates.getCommonNodeWithRepeated(60, 70)); // LCA of 60 and one of the 70s is 70
    }

    @Test
    void testGetCommonNodeWithRepeated_OneIsAncestorOfOtherAndDuplicates() {
        // bstWithDuplicates has 30 (parent) and 30 (child, right of first 30)
        // LCA of 30 and 20 is 30
        assertEquals(30, bstWithDuplicates.getCommonNodeWithRepeated(30, 20)); 
        // LCA of 70 and 60 is 70
        assertEquals(70, bstWithDuplicates.getCommonNodeWithRepeated(70, 60)); 
        // LCA of one 70 and another 70 is 70
        assertEquals(70, bstWithDuplicates.getCommonNodeWithRepeated(70, 70)); 
    }

    @Test
    void testGetCommonNodeWithRepeated_ElementsInDifferentBranchesWithDuplicates() {
        // LCA of 20 (left of 30) and 70 (one of them) is 50 (root)
        assertEquals(50, bstWithDuplicates.getCommonNodeWithRepeated(20, 70));
        // LCA of 40 (right of 30) and 60 (left of 70) is 50 (root)
        assertEquals(50, bstWithDuplicates.getCommonNodeWithRepeated(40, 60));
    }

    @Test
    void testGetCommonNodeWithRepeated_DeepNodesWithDuplicates() {
        // bstWithDuplicates has 10 (left of 20, left of 30) and 30 (which has another 30 as right child)
        // LCA of 10 and 30 is 30
        assertEquals(30, bstWithDuplicates.getCommonNodeWithRepeated(10, 30));
        // LCA of 35 (right of 30) and 80 (right of 70) is 50
        assertEquals(50, bstWithDuplicates.getCommonNodeWithRepeated(35, 80));
    }
} 