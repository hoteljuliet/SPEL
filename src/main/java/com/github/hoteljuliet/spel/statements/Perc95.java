package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdunning.math.stats.TDigest;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;
@Step(tag = "perc95")
public class Perc95 extends StepStatement implements Serializable {
    private final String source;
    private final String dest;
    private final Integer compression;
    private final TDigest tDigest;
    @JsonCreator
    public Perc95(@JsonProperty(value = "source", required = true) String source,
                  @JsonProperty(value = "compression", required = true) Integer compression,
                  @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.compression = compression;
        this.dest = dest;
        this.tDigest = TDigest.createDigest(compression);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Double value = context.getField(source);
        tDigest.add(value);
        context.addField(dest, tDigest.quantile(95.0));
        return EMPTY;
    }
}