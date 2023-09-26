package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * The inverse of remove, keep only those fields specified - remove all others from context
 */
@Step(tag = "keep-only")
public class KeepOnly extends StepStatement implements Serializable {
    private final List<String> values;

    @JsonCreator
    public KeepOnly(@JsonProperty(value = "values", required = true) List<String> values) {
        super();
        this.values = values;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Context newContext = new Context();
        for (String value : values) {
            Object object = context.getField(value);
            newContext.addField(value, object);
        }
        context.replace(newContext.getBacking());
        return EMPTY;
    }
}

