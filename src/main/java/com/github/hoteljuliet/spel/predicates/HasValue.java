package com.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepPredicate;
import com.github.hoteljuliet.spel.StepBase;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "has-value")
public class HasValue extends StepPredicate implements Serializable {
    private final String path;
    private final List<Object> values;

    @JsonCreator
    public HasValue(@JsonProperty(value = "path", required = true) String path,
                    @JsonProperty(value = "values", required = true) List<Object> values) {
        super();
        this.path = path;
        this.values = values;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(path)) {
            Object fieldValue = context.get(path);
            return values.contains(fieldValue) ? StepBase.TRUE : StepBase.FALSE;
        }
        else {
            softFailure();
            return StepBase.FALSE;
        }
    }
}
