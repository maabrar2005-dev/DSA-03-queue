
package oy.tol.tra;

public class QueueImplementation<E> implements QueueInterface<E> {

    private Object[] itemArray;
    private int capacity;
    private int head = 0;
    private int size = 0;

    private static final int DEFAULT_QUEUE_SIZE = 10;

    public QueueImplementation() throws QueueAllocationException {
        this(DEFAULT_QUEUE_SIZE);
    }

    public QueueImplementation(int capacity) throws QueueAllocationException {
        if (capacity < 2) {
            throw new QueueAllocationException(
                "Queue capacity must be at least 2."
            );
        }

        try {
            itemArray = new Object[capacity];
            this.capacity = capacity;
        } catch (OutOfMemoryError e) {
            throw new QueueAllocationException(
                "Unable to allocate queue storage."
            );
        }
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public void enqueue(E element)
            throws QueueAllocationException, NullPointerException {

        if (element == null) {
            throw new NullPointerException(
                "Queue cannot contain null elements."
            );
        }

        if (size == capacity) {
            resize();
        }

        int tail = (head + size) % capacity;
        itemArray[tail] = element;
        size++;
    }

    private void resize() throws QueueAllocationException {
        try {
            int newCapacity = Math.multiplyExact(capacity, 2);
            Object[] newArray = new Object[newCapacity];

            for (int i = 0; i < size; i++) {
                newArray[i] = itemArray[(head + i) % capacity];
            }

            itemArray = newArray;
            capacity = newCapacity;
            head = 0;

        } catch (OutOfMemoryError | ArithmeticException e) {
            throw new QueueAllocationException(
                "Unable to increase queue capacity."
            );
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E dequeue() throws QueueIsEmptyException {

        if (isEmpty()) {
            throw new QueueIsEmptyException(
                "Cannot dequeue from an empty queue."
            );
        }

        E element = (E) itemArray[head];
        itemArray[head] = null;

        head = (head + 1) % capacity;
        size--;

        if (size == 0) {
            head = 0;
        }

        return element;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E element() throws QueueIsEmptyException {

        if (isEmpty()) {
            throw new QueueIsEmptyException(
                "Cannot access an element from an empty queue."
            );
        }

        return (E) itemArray[head];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            itemArray[(head + i) % capacity] = null;
        }

        head = 0;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < size; i++) {
            builder.append(itemArray[(head + i) % capacity]);

            if (i < size - 1) {
                builder.append(", ");
            }
        }

        builder.append("]");
        return builder.toString();
    }
}
