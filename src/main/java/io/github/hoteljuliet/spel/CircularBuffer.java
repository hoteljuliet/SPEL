package io.github.hoteljuliet.spel;

import java.io.Serializable;
import java.lang.reflect.Array;

public class CircularBuffer<T extends Object> implements Serializable {

    private Integer capacity;
    private Integer index;
    private Long rollovers;
    private T[] data;

    public CircularBuffer(Class<T> clazz, Integer capacity) {
        this.capacity = capacity;
        data = (T[]) Array.newInstance(clazz, capacity);
        index = 0;
        rollovers = 0l;
    }

    public void add(T element) {
        if (index == capacity) {
            index = 0;
            rollovers += 1;
        }
        data[index] = element;
        index += 1;
    }

    public T[] getData() {
        return data;
    }

    public Long getRollovers() {
        return rollovers;
    }
}
