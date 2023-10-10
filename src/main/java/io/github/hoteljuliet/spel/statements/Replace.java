package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.Optional;

/**
 * Replace values in a string
 */
@Step(tag = "replace")
public class Replace extends StepStatement implements Serializable {
    private final String in;
    private final TemplateLiteral from;
    private final TemplateLiteral to;

    @JsonCreator
    public Replace(@JsonProperty(value = "in", required = true) String in,
                   @JsonProperty(value = "from", required = true) TemplateLiteral from,
                   @JsonProperty(value = "to", required = true) TemplateLiteral to) {
        super();
        this.in = in;
        this.from = from;
        this.to = to;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String fieldValue = context.getField(in);
        String fromValue = from.get(context);
        String toValue = to.get(context);
        String replaced = fieldValue.replaceAll(fromValue, toValue);
        context.replaceFieldValue(in, replaced);
        return EMPTY;
    }
}


