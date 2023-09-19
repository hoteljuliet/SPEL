package net.hoteljuliet.spel;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.io.Serializable;

public class PipelineMetrics implements Serializable {

    private final StopWatch stopWatch;
    private final SummaryStatistics runTimeNanos;
    private String fatalRootCase;

    public PipelineMetrics() {
        stopWatch = new StopWatch();
        runTimeNanos = new SummaryStatistics();
        fatalRootCase = "";
    }

    public void start() {
        stopWatch.start();
    }

    public void stop() {
        stopWatch.stop();
        runTimeNanos.addValue(stopWatch.getNanoTime().doubleValue());
    }

    public void setFatalRootCase(Exception ex) {
        fatalRootCase = ExceptionUtils.getRootCauseMessage(ex);
    }

    public Long getTotalMillis() {
        return Math.round(runTimeNanos.getSum() / 1000000);
    }

    public Long getAverageMillis() {
        return Math.round(runTimeNanos.getMean() / 1000000);
    }

    public Long getMinMillis() {
        return Math.round(runTimeNanos.getMin() / 1000000);
    }

    public Long getMaxMillis() {
        return Math.round(runTimeNanos.getMax() / 1000000);
    }

    public String getFatalRootCase() {
        return fatalRootCase;
    }
}
