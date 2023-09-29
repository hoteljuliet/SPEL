package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdunning.math.stats.TDigest;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;
@Step(tag = "perc95")
public class Perc95 extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final Integer compression;
    private final TDigest tDigest;
    @JsonCreator
    public Perc95(@JsonProperty(value = "in", required = true) String in,
                  @JsonProperty(value = "compression", required = true) Integer compression,
                  @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.compression = compression;
        this.out = out;
        this.tDigest = TDigest.createDigest(compression);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Double value = context.getField(in);
        tDigest.add(value);
        context.addField(out, tDigest.quantile(95.0));
        return EMPTY;
    }
}