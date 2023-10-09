package io.github.hoteljuliet.spel;

import java.lang.reflect.Array;
import java.util.Optional;

public class CircularBuffer<T extends Object> {

    private int capacity;
    private int readSequence;
    private int writeSequence;
    private T[] data;

    public CircularBuffer(Class<T> clazz, int capacity) {
        this.capacity = capacity;
        this.data = (T[]) Array.newInstance(clazz, capacity);
        this.readSequence = 0;
        this.writeSequence = -1;
    }

    public boolean offer(T element) {
        boolean isFull = (writeSequence - readSequence) + 1 == capacity;
        if (!isFull) {
            int nextWriteSeq = writeSequence + 1;
            data[nextWriteSeq % capacity] = element;
            writeSequence++;
            return true;
        }
        return false;
    }

    public Optional<T> poll() {
        boolean isEmpty = writeSequence < readSequence;
        Optional<T> retVal = Optional.empty();
        if (!isEmpty) {
            T nextValue = data[readSequence % capacity];
            readSequence++;
            retVal = Optional.of(nextValue);
        }
        return retVal;
    }

    public T[] getData() {
        return data;
    }
}
