package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import io.github.hoteljuliet.spel.*;

import java.io.Serializable;
import java.util.Optional;

/**
 * Create a substring from a string
 */
@Step(tag = "substring")
public class SubString extends StepStatement implements Serializable {
    private final String in;
    private final Integer from;
    private final Integer to;
    private final String out;

    @JsonCreator
    public SubString(@JsonProperty(value = "in", required = true) String in,
                     @JsonProperty(value = "from", required = true) Integer from,
                     @JsonProperty(value = "to", required = true) Integer to,
                     @JsonProperty(value = "out", required = true) String out) {
        super();
        Preconditions.checkArgument(from < 0 || to < 0 || from < to, "invalid to/from range");
        this.in = in;
        this.from = from;
        this.to = to;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(in);
        String substring = value.substring(from, to);
        context.addField(out, substring);
        return StepBase.EMPTY;
    }
}
