package net.hoteljuliet.spel.statements;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdunning.math.stats.TDigest;
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
    private final Boolean volatileState;
    private transient HyperLogLogPlus hyperLogLogPlus;

    @JsonCreator
    public Cardinality(@JsonProperty(value = "source", required = true) String source,
                       @JsonProperty(value = "precision", required = true) Integer precision,
                       @JsonProperty(value = "dest", required = true) String dest,
                       @JsonProperty(value = "volatile", required = false, defaultValue = "false") Boolean volatileState) {
        super();
        this.source = source;
        this.precision = precision;
        this.dest = dest;
        this.volatileState = volatileState;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (requiresExternal(context, "hyperLogLogPlus", volatileState)) {
            hyperLogLogPlus = externalize(context, "hyperLogLogPlus", new HyperLogLogPlus(precision), volatileState);
        }

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
