package net.hoteljuliet.spel.statements;

import com.clearspring.analytics.stream.cardinality.HyperLogLogPlus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.util.Optional;

public class Cardinality extends StatementStep implements Serializable {

    private final String source;
    private final String dest;
    private final Integer precision;
    private transient HyperLogLogPlus hyperLogLogPlus;
    private String b64;

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
        return COMMAND_NEITHER;
    }

    @Override
    public void snapshot() {
        try {
            b64 = Base64.encodeBase64String(hyperLogLogPlus.getBytes());
        }
        catch(Exception ex) {
            ;
        }
    }

    @Override
    public void restore() {
        try {
            byte[] bytes = Base64.decodeBase64(b64);
            hyperLogLogPlus = HyperLogLogPlus.Builder.build(bytes);
        }
        catch(Exception ex) {
            ;
        }
    }
}
