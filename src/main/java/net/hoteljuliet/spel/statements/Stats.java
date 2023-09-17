package net.hoteljuliet.spel.statements;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Step(tag = "stats")
public class Stats extends StepStatement implements Serializable {
    private final String source;
    private final String dest;
    private final Boolean volatileState;
    private transient SummaryStatistics summaryStatistics;

    @JsonCreator
    public Stats(@JsonProperty(value = "source", required = true) String source,
                 @JsonProperty(value = "dest", required = true) String dest,
                 @JsonProperty(value = "volatile", required = false, defaultValue = "false") Boolean volatileState) {
        super();
        this.source = source;
        this.dest = dest;
        this.volatileState = volatileState;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (requiresExternal(context, "summaryStatistics", volatileState)) {
            summaryStatistics = externalize(context, "summaryStatistics", new SummaryStatistics(), volatileState);
        }

        if (context.hasField(source)) {
            Double value = context.getField(source);
            summaryStatistics.addValue(value);
            Map<String, Double> map = new HashMap<>();
            map.put("mean", summaryStatistics.getMean());
            map.put("min", summaryStatistics.getMin());
            map.put("max", summaryStatistics.getMax());
            map.put("variance", summaryStatistics.getVariance());
            map.put("sigma", summaryStatistics.getStandardDeviation());
            map.put("sum", summaryStatistics.getSum());
            context.addField(dest, map);
        }
        else {
            missingField();
        }
        return NEITHER;
    }
}