package io.github.hoteljuliet.spel;

import java.util.ArrayList;
import java.util.List;

public class StatementResult {
    public final Long success;
    public final Long exception;
    public final Long softFailure;
    public final Long averageNanos;
    public final Long minNanos;
    public final Long maxNanos;
    public final List<String> mostCommonExceptions;

    public StatementResult(StepBase stepBase) {
        success = stepBase.success.longValue();
        exception = stepBase.exception.longValue();
        softFailure = stepBase.softFailure.longValue();
        averageNanos = Math.round(stepBase.runTimeNanos.getMean());
        minNanos = Math.round(stepBase.runTimeNanos.getMin());
        maxNanos = Math.round(stepBase.runTimeNanos.getMax());
        mostCommonExceptions = new ArrayList<>();
        for (String exception : stepBase.exceptionsCounter.getMap().keySet()) {
            mostCommonExceptions.add(exception);
        }
    }
}
