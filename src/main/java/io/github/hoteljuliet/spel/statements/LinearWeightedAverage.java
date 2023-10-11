package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Calculate a triple exponential weighted moving average
 * See Also: https://github.com/elastic/elasticsearch/blob/9ef5d301c299521bdec62226a639c7c1c296242a/server/src/main/java/org/elasticsearch/search/aggregations/pipeline/MovingFunctions.java
 */
@Step(tag = "linear-average")
public class LinearWeightedAverage extends StepStatement implements Serializable {
    private final String in;
    private final Integer capacity;
    private final String out;
    private final FixedFifo<Double> fixedFifo;

    @JsonCreator
    public LinearWeightedAverage(@JsonProperty(value = "in", required = true) String in,
                                 @JsonProperty(value = "capacity", required = true) Integer capacity,
                                 @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.capacity = capacity;
        this.out = out;
        fixedFifo = new FixedFifo<>(capacity);
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Double value = context.getField(in);
        fixedFifo.add(value);
        Double output = linearWeightedAvg(fixedFifo.getList());
        context.addField(out, output);
        return StepBase.EMPTY;
    }

    /**
     * Calculate a linearly weighted moving average, such that older values are
     * linearly less important.  "Time" is determined by position in collection
     *
     * Only finite values are averaged.  NaN or null are ignored.
     * If all values are missing/null/NaN, the return value will be NaN
     * The average is based on the count of non-null, non-NaN values.
     */
    public static double linearWeightedAvg(List<Double> values) {
        double avg = 0;
        long totalWeight = 1;
        long current = 1;

        for (double v : values) {
            if (Double.isNaN(v) == false) {
                avg += v * current;
                totalWeight += current;
                current += 1;
            }
        }
        return totalWeight == 1 ? Double.NaN : avg / totalWeight;
    }
}
