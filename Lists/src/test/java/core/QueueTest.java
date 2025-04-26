package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QueueTest {
    private Queue<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new Queue<>();
    }

    @Test
    void testEmptyQueue() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        assertThrows(IllegalStateException.class, () -> queue.dequeue());
        assertThrows(IllegalStateException.class, () -> queue.peek());
    }

    @Test
    void testEnqueueAndDequeue() {
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals(1, queue.peek());

        queue.enqueue(2);
        assertEquals(2, queue.size());
        assertEquals(1, queue.peek());

        assertEquals(1, queue.dequeue());
        assertEquals(1, queue.size());
        assertEquals(2, queue.peek());

        assertEquals(2, queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    void testMultipleOperations() {
        // Enqueue multiple elements
        for (int i = 1; i <= 5; i++) {
            queue.enqueue(i);
        }
        assertEquals(5, queue.size());

        // Dequeue some elements
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.size());

        // Enqueue more elements
        queue.enqueue(6);
        queue.enqueue(7);
        assertEquals(5, queue.size());

        // Dequeue remaining elements
        assertEquals(3, queue.dequeue());
        assertEquals(4, queue.dequeue());
        assertEquals(5, queue.dequeue());
        assertEquals(6, queue.dequeue());
        assertEquals(7, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    void testPeek() {
        queue.enqueue(10);
        assertEquals(10, queue.peek());
        queue.enqueue(20);
        assertEquals(10, queue.peek());
        queue.dequeue();
        assertEquals(20, queue.peek());
    }

    @Test
    void testCircularBehavior() {
        // Test circular behavior with multiple enqueues and dequeues
        for (int i = 1; i <= 3; i++) {
            queue.enqueue(i);
        }
        
        assertEquals(1, queue.dequeue());
        queue.enqueue(4);
        assertEquals(2, queue.dequeue());
        queue.enqueue(5);
        
        assertEquals(3, queue.dequeue());
        assertEquals(4, queue.dequeue());
        assertEquals(5, queue.dequeue());
        assertTrue(queue.isEmpty());
    }
} 