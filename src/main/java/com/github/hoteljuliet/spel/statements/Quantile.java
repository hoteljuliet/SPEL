package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.tdunning.math.stats.TDigest;
import com.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Step(tag = "quantile")
public class Quantile extends StepStatement implements Serializable {
    private final String source;
    private final String dest;
    private final Integer compression;
    private final List<Double> quantiles;
    private final TDigest tDigest;

    @JsonCreator
    public Quantile(@JsonProperty(value = "source", required = true) String source,
                    @JsonProperty(value = "compression", required = true) Integer compression,
                    @JsonProperty(value = "quantiles", required = true) List<Double> quantiles,
                    @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.compression = compression;
        this.quantiles = quantiles;
        this.dest = dest;
        this.tDigest = TDigest.createDigest(compression);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Double value = context.getField(source);
        tDigest.add(value);

        Map<String, Double> map = new HashMap<>();
        for (Double d : quantiles) {
            map.put(String.valueOf(d), tDigest.quantile(d));
        }
        context.addField(dest, map);
        return EMPTY;
    }
}