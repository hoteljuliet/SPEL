package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdunning.math.stats.TDigest;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Step(tag = "quantile")
public class Quantile extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final Integer compression;
    private final List<Double> quantiles;
    private final TDigest tDigest;

    @JsonCreator
    public Quantile(@JsonProperty(value = "in", required = true) String in,
                    @JsonProperty(value = "compression", required = true) Integer compression,
                    @JsonProperty(value = "quantiles", required = true) List<Double> quantiles,
                    @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.compression = compression;
        this.quantiles = quantiles;
        this.out = out;
        this.tDigest = TDigest.createDigest(compression);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Double value = context.getField(in);
        tDigest.add(value);

        Map<String, Double> map = new HashMap<>();
        for (Double d : quantiles) {
            map.put(String.valueOf(d), tDigest.quantile(d));
        }
        context.addField(out, map);
        return EMPTY;
    }
}