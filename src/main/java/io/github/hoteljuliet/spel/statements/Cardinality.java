package io.github.hoteljuliet.spel.statements;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;

/**
 * Add the cardinality of a field
 */
@Step(tag = "cardinality")
public class Cardinality extends StepStatement implements Serializable {

    private final String in;
    private final String out;
    private final Integer precision;
    private final HyperLogLogPlus hyperLogLogPlus;

    /**
     * Compute the cardinality of a string and put the result into the context
     * Based on https://github.com/addthis/stream-lib/blob/master/src/main/java/com/clearspring/analytics/stream/cardinality/HyperLogLogPlus.java
     * @param in the path to a String value in the context
     * @param precision an integer in the range 4-18
     * @param out the path in the context where the computed cardinality will be placed
     */
    @JsonCreator
    public Cardinality(@JsonProperty(value = "in", required = true) String in,
                       @JsonProperty(value = "precision", required = true) Integer precision,
                       @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.precision = precision;
        this.out = out;
        this.hyperLogLogPlus = new HyperLogLogPlus(precision);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object target = context.getField(in);
        Boolean ignored = hyperLogLogPlus.offer(target);
        context.addField(out, hyperLogLogPlus.cardinality());
        return EMPTY;
    }
}
