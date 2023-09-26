package com.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepPredicate;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "hash-sample")
public class HashedSample extends StepPredicate implements Serializable {
    private final Integer percentage;
    private final String path;

    @JsonCreator
    public HashedSample(@JsonProperty(value = "pct", required = true) Integer percentage,
                        @JsonProperty(value = "path", required = true) String path) {
        super();
        this.percentage = percentage;
        this.path = path;
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(path);
        return (value.hashCode() % 100 < percentage) ? TRUE : FALSE;
    }
}
