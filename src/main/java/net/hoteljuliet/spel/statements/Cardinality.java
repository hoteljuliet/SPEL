package net.hoteljuliet.spel.statements;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "cardinality")
public class Cardinality extends StepStatement implements Serializable {

    private final String source;
    private final String dest;
    private final Integer precision;
    // TODO: externalize this into the context
    private transient HyperLogLogPlus hyperLogLogPlus;

    @JsonCreator
    public Cardinality(@JsonProperty(value = "source", required = true) String source,
                       @JsonProperty(value = "precision", required = true) Integer precision,
                       @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.precision = precision;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        hyperLogLogPlus = (HyperLogLogPlus) externalized(context, "hyperLogLogPlus", new HyperLogLogPlus(precision));

        if (context.hasField(source)) {
            Object value = context.getField(source);
            Boolean result = hyperLogLogPlus.offer(value);
            context.addField(dest, hyperLogLogPlus.cardinality());
        }
        else {
            missingField();
        }
        return NEITHER;
    }
}
