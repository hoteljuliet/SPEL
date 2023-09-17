package net.hoteljuliet.spel;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class StepMetrics {

    private final LongAdder evalTrue;
    private final LongAdder evalFalse;

    private final StopWatch stopWatch;
    private final SummaryStatistics runTimeNanos;
    private final AtomicLong lastRunNanos;
    private final LongAdder success;
    private final LongAdder missingField;
    private final LongAdder exceptionThrown;
    private final LongAdder softFailure;
    private final LimitedCountingMap exceptionsCounter;

    public StepMetrics() {
        stopWatch = new StopWatch();
        runTimeNanos = new SummaryStatistics();
        lastRunNanos = new AtomicLong();
        success = new LongAdder();
        missingField = new LongAdder();
        exceptionThrown = new LongAdder();
        softFailure = new LongAdder();
        evalTrue = new LongAdder();
        evalFalse = new LongAdder();
        exceptionsCounter = new LimitedCountingMap();
    }

    public StopWatch getStopWatch() {
        return stopWatch;
    }

    public SummaryStatistics getRunTimeNanos() {
        return runTimeNanos;
    }

    public AtomicLong getLastRunNanos() {
        return lastRunNanos;
    }

    public LongAdder getSuccess() {
        return success;
    }

    public LongAdder getMissingField() {
        return missingField;
    }

    public LongAdder getExceptionThrown() {
        return exceptionThrown;
    }

    public LongAdder getSoftFailure() {
        return softFailure;
    }

    public LimitedCountingMap getExceptionsCounter() {
        return exceptionsCounter;
    }

    public LongAdder getEvalTrue() {
        return evalTrue;
    }

    public LongAdder getEvalFalse() {
        return evalFalse;
    }

    // TODO: add toString()
}
