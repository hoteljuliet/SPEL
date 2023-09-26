package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;

/**
 * Remove all whitespace from a string
 */
@Step(tag = "strip")
public class Strip extends StepStatement implements Serializable {
    private final String value;

    @JsonCreator
    public Strip(@JsonProperty(value = "value", required = true) String value) {
        super();
        this.value = value;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String v = context.getField(value);
        String stripped = v.replaceAll("\\s+","");
        context.replaceFieldValue(value, stripped);
        return EMPTY;
    }
}

