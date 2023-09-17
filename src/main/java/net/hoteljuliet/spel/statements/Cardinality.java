package net.hoteljuliet.spel.statements;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "cardinality")
public class Cardinality extends StepStatement implements Serializable {

    private final String source;
    private final String dest;
    private final Integer precision;
    private final HyperLogLogPlus hyperLogLogPlus;

    @JsonCreator
    public Cardinality(@JsonProperty(value = "source", required = true) String source,
                       @JsonProperty(value = "precision", required = true) Integer precision,
                       @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.precision = precision;
        this.dest = dest;
        hyperLogLogPlus = new HyperLogLogPlus(precision);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            Object value = context.getField(source);
            Boolean result = hyperLogLogPlus.offer(value);
            context.addField(dest, hyperLogLogPlus.cardinality());
        }
        else {
            missingField.increment();
        }
        return NEITHER;
    }
}
