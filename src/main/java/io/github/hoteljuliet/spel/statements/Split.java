package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Splits a string on a delimiter. The opposite of concat.
 */
@Step(tag = "split")
public class Split extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final String delimiter;

    @JsonCreator
    public Split(@JsonProperty(value = "in", required = true) String in,
                 @JsonProperty(value = "delimiter", required = true) String delimiter,
                 @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.delimiter = delimiter;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(in);
        String[] split = value.split(delimiter);
        List<String> output = Arrays.asList(split);
        context.addField(out, output);
        return StepBase.EMPTY;
    }
}
