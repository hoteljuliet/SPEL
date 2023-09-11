package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Stats extends StatementStep implements Serializable {
    private final String source;
    private final String dest;
    private final SummaryStatistics summaryStatistics;
    @JsonCreator
    public Stats(@JsonProperty(value = "source", required = true) String source,
                 @JsonProperty(value = "dest", required = true) String dest) {
        this.source = source;
        this.dest = dest;
        summaryStatistics = new SummaryStatistics();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
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
            context.missingField(name);
        }
        return COMMAND_NEITHER;
    }
}