package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.Optional;

/**
 * Replace values in a string
 */
@Step(tag = "replace")
public class Replace extends StepStatement implements Serializable {
    private final String value;
    private final TemplateLiteral from;
    private final TemplateLiteral to;

    @JsonCreator
    public Replace(@JsonProperty(value = "value", required = true) String value,
                   @JsonProperty(value = "from", required = true) TemplateLiteral from,
                   @JsonProperty(value = "to", required = true) TemplateLiteral to) {
        super();
        this.value = value;
        this.from = from;
        this.to = to;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String fieldValue = context.getField(value);
        String fromValue = from.get(context);
        String toValue = to.get(context);
        String replaced = fieldValue.replaceAll(fromValue,toValue);
        context.replaceFieldValue(value, replaced);
        return EMPTY;
    }
}


