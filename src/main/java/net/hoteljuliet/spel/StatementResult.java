package net.hoteljuliet.spel;

import java.util.ArrayList;
import java.util.List;

public class StatementResult {
    private final Long success;
    private final Long exception;
    private final Long softFailure;
    private final Long averageNanos;
    private final Long minNanos;
    private final Long maxNanos;
    private final List<String> mostCommonExceptions;

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
