package io.github.hoteljuliet.spel.statements;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;

/**
 * Add the cardinality of a field
 */
@Step(tag = "cardinality")
public class Cardinality extends StepStatement implements Serializable {

    private final String value;
    private final String dest;
    private final Integer precision;
    private final HyperLogLogPlus hyperLogLogPlus;

    @JsonCreator
    public Cardinality(@JsonProperty(value = "value", required = true) String value,
                       @JsonProperty(value = "precision", required = true) Integer precision,
                       @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.value = value;
        this.precision = precision;
        this.dest = dest;
        this.hyperLogLogPlus = new HyperLogLogPlus(precision);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object target = context.getField(value);
        Boolean ignored = hyperLogLogPlus.offer(target);
        context.addField(dest, hyperLogLogPlus.cardinality());
        return EMPTY;
    }
}
