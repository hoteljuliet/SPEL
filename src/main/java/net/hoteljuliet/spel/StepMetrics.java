package net.hoteljuliet.spel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class StepMetrics {

    // TODO: when run in watchdog, calculate number of overtime runs
    // public LongAdder overTime = new LongAdder();
    public LongAdder evalTrue = new LongAdder();
    public LongAdder evalFalse = new LongAdder();
    public LongAdder success = new LongAdder();
    public LongAdder missingField = new LongAdder();
    public LongAdder exceptionThrown = new LongAdder();
    public LongAdder softFailure = new LongAdder();
    public SummaryStatistics runTimeNanos = new SummaryStatistics();
    public AtomicLong lastRunNanos = new AtomicLong();
    public LimitedCountingMap exceptionsCounter = new LimitedCountingMap();

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("evalTrue: ").append(evalTrue.longValue());
        stringBuilder.append(", evalFalse: ").append(evalTrue.longValue());
        stringBuilder.append(", success: ").append(success.longValue());
        stringBuilder.append(", missingField: ").append(missingField.longValue());
        stringBuilder.append(", exceptionThrown: ").append(exceptionThrown.longValue());
        stringBuilder.append(", softFailure: ").append(softFailure.longValue());
        stringBuilder.append(", runTimeNanos (Mean): ").append(runTimeNanos.getMean());
        stringBuilder.append(", runTimeNanos (Min): ").append(runTimeNanos.getMin());
        stringBuilder.append(", runTimeNanos (Max): ").append(runTimeNanos.getMax());
        stringBuilder.append(", exceptionsCounter: ").append(exceptionsCounter.getMap());
        return stringBuilder.toString();
    }
}
