package net.hoteljuliet.spel;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public class StopWatch implements Serializable {
    private Instant start;
    private Instant stop;
    private AtomicBoolean running;

    public StopWatch() {
        running = new AtomicBoolean();
    }

    public void start() {
        start = Instant.now();
        running.set(true);
    }

    public void stop() {
        if (running.get()) {
            running.set(false);
            stop = Instant.now();
        }
    }

    public Integer getNanoTime() {
        Duration duration = Duration.between(start, stop);
        return duration.getNano();
    }
}

