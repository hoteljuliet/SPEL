package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Append a value to a list
 */
@Step(tag = "append")
public class Append extends StepStatement implements Serializable {
    private final TemplateLiteral in;
    private final String out;


    /**
     * Append a value to a List in the context
     * @param in the value to append. See {@link io.github.hoteljuliet.spel.mustache.TemplateLiteral}
     * @param out the path into the context to a List where the value will be appended
     */
    @JsonCreator
    public Append(@JsonProperty(value = "in", required = true) TemplateLiteral in,
                  @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object fieldValue = in.get(context);
        List<Object> listValue = context.getField(out);
        listValue.add(fieldValue);
        context.replaceFieldValue(out, listValue);
        return StepBase.EMPTY;
    }
}
