package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdunning.math.stats.TDigest;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

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
    private final Boolean volatileState;
    private transient TDigest tDigest;
    @JsonCreator
    public Quantile(@JsonProperty(value = "source", required = true) String source,
                    @JsonProperty(value = "compression", required = true) Integer compression,
                    @JsonProperty(value = "quantiles", required = true) List<Double> quantiles,
                    @JsonProperty(value = "dest", required = true) String dest,
                    @JsonProperty(value = "volatile", required = false, defaultValue = "false") Boolean volatileState) {
        super();
        this.source = source;
        this.compression = compression;
        this.quantiles = quantiles;
        this.dest = dest;
        this.volatileState = volatileState;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (requiresExternal(context, "tDigest", volatileState)) {
            tDigest = externalize(context, "tDigest", TDigest.createDigest(compression), volatileState);
        }

        if (context.hasField(source)) {
            Double value = context.getField(source);
            tDigest.add(value);

            Map<String, Double> map = new HashMap<>();
            for (Double d : quantiles) {
                map.put(String.valueOf(d), tDigest.quantile(d));
            }
            context.addField(dest, map);
        } else {
            missingField();
        }
        return NEITHER;
    }
}