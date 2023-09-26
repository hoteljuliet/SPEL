package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepBase;
import com.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Splits a string on a delimiter. The opposite of concat.
 */
@Step(tag = "split")
public class Split extends StepStatement implements Serializable {
    private final String source;
    private final String dest;
    private final String delimiter;

    @JsonCreator
    public Split(@JsonProperty(value = "source", required = true) String source,
                 @JsonProperty(value = "delimiter", required = true) String delimiter,
                 @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.delimiter = delimiter;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(source);
        String[] split = value.split(delimiter);
        List<String> output = Arrays.asList(split);
        context.addField(dest, output);
        return StepBase.EMPTY;
    }
}
