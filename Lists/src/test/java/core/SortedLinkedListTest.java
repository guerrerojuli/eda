package core;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedLinkedListTest {
    private SortedLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new SortedLinkedList<>();
    }

    @Test
    void testInsert() {
        // Caso null
        assertThrows(IllegalArgumentException.class, () -> list.insert(null));
        
        // Caso lista vacía
        assertTrue(list.insert(5));
        assertEquals(1, list.size());
        assertEquals(Integer.valueOf(5), list.getMin());
        assertEquals(Integer.valueOf(5), list.getMax());
        
        // Caso duplicado
        assertFalse(list.insert(5));
        assertEquals(1, list.size());
        
        // Caso inserción ordenada
        assertTrue(list.insert(7));
        assertTrue(list.insert(3));
        assertEquals(3, list.size());
        assertEquals(Integer.valueOf(3), list.getMin());
        assertEquals(Integer.valueOf(7), list.getMax());
    }

    @Test
    void testFind() {
        // Caso lista vacía
        assertFalse(list.find(5));
        
        // Caso existente
        list.insert(5);
        list.insert(7);
        assertTrue(list.find(5));
        assertTrue(list.find(7));
        
        // Caso no existente
        assertFalse(list.find(3));
    }

    @Test
    void testRemove() {
        // Caso null
        assertThrows(IllegalArgumentException.class, () -> list.remove(null));
        
        // Caso lista vacía
        assertFalse(list.remove(5));
        
        // Caso existente
        list.insert(5);
        list.insert(7);
        list.insert(3);
        assertTrue(list.remove(5));
        assertEquals(2, list.size());
        assertEquals(Integer.valueOf(3), list.getMin());
        assertEquals(Integer.valueOf(7), list.getMax());
        
        // Caso no existente
        assertFalse(list.remove(4));
        assertEquals(2, list.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(list.isEmpty());
        list.insert(5);
        assertFalse(list.isEmpty());
        list.remove(5);
        assertTrue(list.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, list.size());
        list.insert(5);
        assertEquals(1, list.size());
        list.insert(7);
        assertEquals(2, list.size());
        list.remove(5);
        assertEquals(1, list.size());
    }

    @Test
    void testGetMinMax() {
        // Caso lista vacía
        assertNull(list.getMin());
        assertNull(list.getMax());
        
        // Caso un elemento
        list.insert(5);
        assertEquals(Integer.valueOf(5), list.getMin());
        assertEquals(Integer.valueOf(5), list.getMax());
        
        // Caso múltiples elementos
        list.insert(7);
        list.insert(3);
        assertEquals(Integer.valueOf(3), list.getMin());
        assertEquals(Integer.valueOf(7), list.getMax());
    }

    @Test
    void testEquals() {
        SortedLinkedList<Integer> otherList = new SortedLinkedList<>();
        
        // Caso misma lista
        assertTrue(list.equals(otherList));
        
        // Caso listas iguales
        list.insert(5);
        otherList.insert(5);
        assertTrue(list.equals(otherList));
        
        // Caso diferentes tamaños
        otherList.insert(7);
        assertFalse(list.equals(otherList));
        
        // Caso listas iguales con elementos insertados en diferente orden
        list.insert(7);
        assertTrue(list.equals(otherList));
        
        // Caso diferentes elementos
        list.insert(3);
        otherList.insert(8);
        assertFalse(list.equals(otherList));
    }

    @Test
    void testIterator() {
        // Caso lista vacía
        Iterator<Integer> emptyIterator = list.iterator();
        assertFalse(emptyIterator.hasNext());
        assertThrows(NoSuchElementException.class, () -> emptyIterator.next());
        
        // Caso con elementos
        list.insert(5);
        list.insert(7);
        list.insert(3);
        
        Integer[] expected = {3, 5, 7};
        int i = 0;
        for (Integer value : list) {
            assertEquals(expected[i++], value);
        }
    }

    @Test
    void testIteratorRemove() {
        // Caso de uso 1: borrar elementos en posiciones impares
        list.insert(1);
        list.insert(2);
        list.insert(3);
        list.insert(4);
        list.insert(5);
        
        final Iterator<Integer> iter = list.iterator();
        int rec = 0;
        while (iter.hasNext()) {
            iter.next();
            if (rec % 2 == 1) {
                iter.remove();
            }
            rec++;
        }
        
        // Verificar que quedaron los elementos en posiciones pares
        assertEquals(3, list.size());
        assertTrue(list.find(1));
        assertTrue(list.find(3));
        assertTrue(list.find(5));
        assertFalse(list.find(2));
        assertFalse(list.find(4));

        // Caso de uso 2: borrar elementos específicos con límite de iteraciones
        list = new SortedLinkedList<>();
        list.insert(1);
        list.insert(2);
        list.insert(3);
        list.insert(4);
        list.insert(5);
        
        final Iterator<Integer> iter2 = list.iterator();
        int count = 1;
        while (iter2.hasNext()) {
            if (count++ >= 4) break;
            Integer value = iter2.next();
            if (value == 2 || value == 4) {
                iter2.remove();
            }
        }
        
        // Verificar que se eliminaron los elementos correctos
        assertEquals(4, list.size());
        assertTrue(list.find(1));
        assertTrue(list.find(3));
        assertTrue(list.find(5));
        assertTrue(list.find(4));
        assertFalse(list.find(2));

        // Caso de uso 3: borrar el primer elemento
        list = new SortedLinkedList<>();
        list.insert(1);
        list.insert(2);
        list.insert(3);
        
        final Iterator<Integer> iter3 = list.iterator();
        iter3.next();
        iter3.remove();
        
        assertEquals(2, list.size());
        assertFalse(list.find(1));
        assertTrue(list.find(2));
        assertTrue(list.find(3));
        assertEquals(Integer.valueOf(2), list.getMin());
        assertEquals(Integer.valueOf(3), list.getMax());

        // Caso de uso 4: borrar el último elemento
        list = new SortedLinkedList<>();
        list.insert(1);
        list.insert(2);
        list.insert(3);
        
        final Iterator<Integer> iter4 = list.iterator();
        while (iter4.hasNext()) {
            Integer value = iter4.next();
            if (value == 3) {
                iter4.remove();
            }
        }
        
        assertEquals(2, list.size());
        assertTrue(list.find(1));
        assertTrue(list.find(2));
        assertFalse(list.find(3));
        assertEquals(Integer.valueOf(1), list.getMin());
        assertEquals(Integer.valueOf(2), list.getMax());

        // Casos de error
        final Iterator<Integer> iter5 = list.iterator();
        assertThrows(IllegalStateException.class, () -> iter5.remove());
        
        iter5.next();
        iter5.remove();
        assertThrows(IllegalStateException.class, () -> iter5.remove());
    }
} 