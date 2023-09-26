package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepBase;
import com.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;

/**
 * Create a substring from a string
 */
@Step(tag = "substring")
public class SubString extends StepStatement implements Serializable {
    private final String source;
    private final String dest;
    private final Integer from;
    private final Integer to;

    @JsonCreator
    public SubString(@JsonProperty(value = "source", required = true) String source,
                     @JsonProperty(value = "dest", required = true) String dest,
                     @JsonProperty(value = "from", required = true) Integer from,
                     @JsonProperty(value = "to", required = true) Integer to) {
        super();
        this.source = source;
        this.dest = dest;
        this.from = from;
        this.to = to;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(source);
        String substring = value.substring(from, to);
        context.addField(dest, substring);
        return StepBase.EMPTY;
    }
}
