package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.google.common.base.Joiner;
import com.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Concatenates multiple strings into one, separated by a delimiter. The opposite of split.
 */
@Step(tag = "concat")
public class Concat extends StepStatement implements Serializable {

    private final List<String> values;
    private final String delimiter;
    private final String dest;
    @JsonCreator
    public Concat(@JsonProperty(value = "values", required = true) List<String> values,
                  @JsonProperty(value = "dest", required = true) String dest,
                  @JsonProperty(value = "delimiter", required = true) String delimiter) {
        super();
        this.values = values;
        this.delimiter = delimiter;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        List<String> strings = new ArrayList<>();
        for (String value : values) {
            String fieldValue = context.getField(value);
            strings.add(fieldValue);
        }
        context.addField(dest, Joiner.on(delimiter).join(strings));
        return EMPTY;
    }
}

