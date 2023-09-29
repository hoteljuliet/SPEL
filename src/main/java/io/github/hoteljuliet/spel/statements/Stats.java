package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.Step;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Step(tag = "stats")
public class Stats extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final SummaryStatistics summaryStatistics;

    @JsonCreator
    public Stats(@JsonProperty(value = "in", required = true) String in,
                 @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.out = out;
        this.summaryStatistics = new SummaryStatistics();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Double value = context.getField(in);
        summaryStatistics.addValue(value);
        Map<String, Double> map = new HashMap<>();
        map.put("mean", summaryStatistics.getMean());
        map.put("min", summaryStatistics.getMin());
        map.put("max", summaryStatistics.getMax());
        map.put("variance", summaryStatistics.getVariance());
        map.put("sigma", summaryStatistics.getStandardDeviation());
        map.put("sum", summaryStatistics.getSum());
        context.addField(out, map);
        return EMPTY;
    }

    @Override
    public void clear() {
        summaryStatistics.clear();
    }
}