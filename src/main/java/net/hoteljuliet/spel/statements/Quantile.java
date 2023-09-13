package net.hoteljuliet.spel.statements;

import com.clearspring.analytics.stream.quantile.TDigest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Quantile extends StatementStep implements Serializable {
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
        tDigest = new TDigest(compression);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            Double value = context.getField(source);
            tDigest.add(value);

            Map<String, Double> map = new HashMap<>();
            for (Double d : quantiles) {
                map.put(String.valueOf(d), tDigest.quantile(d));
            }
            context.addField(dest, map);
        } else {
            missingField.increment();
        }
        return COMMAND_NEITHER;
    }
}