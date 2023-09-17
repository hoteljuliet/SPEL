package net.hoteljuliet.spel;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

public class StopWatch implements Serializable {
    private Instant start;
    private Instant stop;

    public StopWatch() {
        ;
    }

    public void start() {
        start = Instant.now();
    }

    public void stop() {
        stop = Instant.now();
    }

    public Integer getNanoTime() {
        Duration duration = Duration.between(start, stop);
        return duration.getNano();
    }
}

