package com.github.hoteljuliet.spel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.atomic.LongAdder;

public class LimitedCountingMap implements Serializable {

    private final Integer maxSize;
    private final HashMap<String, LongAdder> map;

    public LimitedCountingMap() {
        map = new HashMap<>();
        this.maxSize = 16;
    }

    public LimitedCountingMap(Integer maxSize) {
        this.maxSize = maxSize;
        map = new HashMap<>();
    }

    public void put(String key) {
        if (map.size() < maxSize) {
            LongAdder count;
            if (map.containsKey(key)) {
                count = map.get(key);
            }
            else {
                count = new LongAdder();
            }
            count.increment();
            map.put(key, count);
        }
    }

    public HashMap<String, LongAdder> getMap() {
        return map;
    }
}
