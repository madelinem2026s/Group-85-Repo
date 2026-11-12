package robotassembly;

import java.util.NoSuchElementException;

/** A singly-linked FIFO queue independent of the game and UI. */
public class LinkedQueue<T> {
    private static class Node<E> {
        E data;
        Node<E> next;
        Node(E data) { this.data = data; }
    }
    private Node<T> front;
    private Node<T> back;
    private int size;

    public void enqueue(T item) {
        if (item == null) throw new IllegalArgumentException("Null items are not allowed");
        Node<T> node = new Node<>(item);
        if (back == null) front = node;
        else back.next = node;
        back = node;
        size++;
    }
    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        return front.data;
    }
    public T dequeue() {
        T result = peek();
        front = front.next;
        size--;
        if (front == null) back = null;
        return result;
    }
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
}
