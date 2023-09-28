package io.github.hoteljuliet.spel.metrics;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public interface MetricsProvider {

    public static final String INVOCATIONS = "invocations";
    public static final String EXCEPTION = "exception";
    public static final String SUCCESS = "success";
    public static final String SOFT_FAIL = "softFailure";

    public static final String TOTAL_NS = "totalNs";
    public static final String MAX_NS = "maxNs";
    public static final String AVG_NS = "avgNs";
    public static final String EVAL_TRUE = "evalTrue";
    public static final String EVAL_FALSE = "evalFalse";

    // name of the component, name of the metric for that component
    AtomicLong provideNext(String name, String metricName);

    // given the name of a component and metric, return the value
    AtomicLong getMetric(String name, String metricName);

    // given the name of a metric, return all component name -> metric pairs
    Map<String, AtomicLong> getAllByMetricName(String metricName);

    // given the name of a component, return all metric name -> metric pairs
    Map<String, AtomicLong> getAllByName(String name);
}
