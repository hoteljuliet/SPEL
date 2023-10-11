package io.github.hoteljuliet.spel;

import java.util.LinkedList;

public class FixedFifo<T> {

    private LinkedList list;
    private Integer capacity;

    public FixedFifo(Integer capacity) {
        list = new LinkedList();
        this.capacity = capacity;
    }

    public void add(T value) {
        if (list.size() >= capacity) {
            list.removeLast();
        }
        list.addFirst(value);
    }

    public LinkedList<T> getList() {
        return list;
    }
}
