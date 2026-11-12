
package robotassembly;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedQueueTest {

    private LinkedQueue<String> queue;

    @BeforeEach
    public void setUp() {
        queue = new LinkedQueue<>();
    }

    @Test
    public void testNewQueueIsEmpty() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Head");

        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals("Head", queue.peek());
    }

    @Test
    public void testEnqueueMultiple() {
        queue.enqueue("Head");
        queue.enqueue("Left Arm");
        queue.enqueue("Right Leg");

        assertEquals(3, queue.size());
        assertEquals("Head", queue.peek());
    }

    @Test
    public void testDequeueFIFO() {
        queue.enqueue("Head");
        queue.enqueue("Left Arm");
        queue.enqueue("Right Leg");

        assertEquals("Head", queue.dequeue());
        assertEquals("Left Arm", queue.dequeue());
        assertEquals("Right Leg", queue.dequeue());

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    public void testPeekDoesNotRemove() {
        queue.enqueue("Head");
        queue.enqueue("Left Arm");

        assertEquals("Head", queue.peek());
        assertEquals("Head", queue.peek());
        assertEquals(2, queue.size());
    }

    @Test
    public void testDequeueEmptyQueue() {
        assertThrows(
            NoSuchElementException.class,
            () -> queue.dequeue()
        );
    }

    @Test
    public void testPeekEmptyQueue() {
        assertThrows(
            NoSuchElementException.class,
            () -> queue.peek()
        );
    }

    @Test
    public void testEnqueueNull() {
        assertThrows(
            IllegalArgumentException.class,
            () -> queue.enqueue(null)
        );
    }

    @Test
    public void testReuseQueueAfterEmptying() {
        queue.enqueue("Head");
        assertEquals("Head", queue.dequeue());

        queue.enqueue("Right Arm");

        assertEquals(1, queue.size());
        assertEquals("Right Arm", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testSizeUpdatesCorrectly() {
        queue.enqueue("Head");
        queue.enqueue("Left Arm");
        assertEquals(2, queue.size());

        queue.dequeue();
        assertEquals(1, queue.size());

        queue.dequeue();
        assertEquals(0, queue.size());
    }
}
