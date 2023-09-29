package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;

/**
 * Remove all whitespace from a string
 */
@Step(tag = "strip")
public class Strip extends StepStatement implements Serializable {
    private final String in;

    @JsonCreator
    public Strip(@JsonProperty(value = "in", required = true) String in) {
        super();
        this.in = in;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(in);
        String stripped = value.replaceAll("\\s+","");
        context.replaceFieldValue(in, stripped);
        return EMPTY;
    }
}

