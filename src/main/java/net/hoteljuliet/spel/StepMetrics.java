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
        ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toStringBuilder.append("evalTrue", evalTrue.longValue());
        toStringBuilder.append("evalFalse", evalTrue.longValue());
        toStringBuilder.append("success", success.longValue());
        toStringBuilder.append("missingField", missingField.longValue());
        toStringBuilder.append("exceptionThrown", exceptionThrown.longValue());
        toStringBuilder.append("softFailure", softFailure.longValue());
        toStringBuilder.append("runTimeNanos (Mean)", runTimeNanos.getMean());
        toStringBuilder.append("runTimeNanos (Min)", runTimeNanos.getMin());
        toStringBuilder.append("runTimeNanos (Max)", runTimeNanos.getMax());
        toStringBuilder.append("exceptionsCounter", exceptionsCounter.toString());
        return toStringBuilder.build();
    }
}
