package io.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepPredicate;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "hash-sample")
public class HashedSample extends StepPredicate implements Serializable {
    private final Integer percentage;
    private final String in;

    @JsonCreator
    public HashedSample(@JsonProperty(value = "pct", required = true) Integer percentage,
                        @JsonProperty(value = "in", required = true) String in) {
        super();
        this.percentage = percentage;
        this.in = in;
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(in);
        return (java.lang.Math.abs(value.hashCode()) % 100 < percentage) ? TRUE : FALSE;
    }
}
