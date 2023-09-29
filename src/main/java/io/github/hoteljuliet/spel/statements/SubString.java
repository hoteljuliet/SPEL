package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;

/**
 * Create a substring from a string
 */
@Step(tag = "substring")
public class SubString extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final Integer from;
    private final Integer to;

    @JsonCreator
    public SubString(@JsonProperty(value = "in", required = true) String in,
                     @JsonProperty(value = "out", required = true) String out,
                     @JsonProperty(value = "from", required = true) Integer from,
                     @JsonProperty(value = "to", required = true) Integer to) {
        super();
        this.in = in;
        this.out = out;
        this.from = from;
        this.to = to;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(in);
        String substring = value.substring(from, to);
        context.addField(out, substring);
        return StepBase.EMPTY;
    }
}
