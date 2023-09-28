package io.github.hoteljuliet.spel.metrics;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultMetricsProvider implements MetricsProvider, Serializable {

    public Map<Pair<String, String>, AtomicLong> metrics;

    public DefaultMetricsProvider() {
        metrics = new HashMap<>();
    }

    @Override
    public AtomicLong provideNext(String name, String metricName) {
        var atomicLong = new AtomicLong();
        metrics.put(new ImmutablePair<>(name, metricName), atomicLong);
        return atomicLong;
    }

    @Override
    public AtomicLong getMetric(String name, String metricName) {
        return metrics.get(new ImmutablePair<>(name, metricName));
    }

    @Override
    public Map<String, AtomicLong> getAllByMetricName(String metricName) {
        Map<String, AtomicLong> retVal = new HashMap<>();
        for (Map.Entry<Pair<String, String>, AtomicLong> entry : metrics.entrySet()) {
            if (entry.getKey().getRight().contains(metricName)) {
                retVal.put(entry.getKey().getLeft(), entry.getValue());
            }
        }
        return retVal;
    }

    @Override
    public Map<String, AtomicLong> getAllByName(String name) {
        Map<String, AtomicLong> retVal = new HashMap<>();
        for (Map.Entry<Pair<String, String>, AtomicLong> entry : metrics.entrySet()) {
            if (entry.getKey().getLeft().contains(name)) {
                retVal.put(entry.getKey().getRight(), entry.getValue());
            }
        }
        return retVal;
    }
}
