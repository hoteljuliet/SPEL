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
 * Add a field, either a literal or from a template {{fieldX}} - but just a single value.
 * See render for full mustache support.
 */
@Step(tag = "add")
public class Add extends StepStatement implements Serializable {
    private final TemplateLiteral in;
    private final String out;


    /**
     * Add a value to the context
     * @param in the value to add. See {@link io.github.hoteljuliet.spel.mustache.TemplateLiteral}
     * @param out the path into the context where the value will be added
     */
    @JsonCreator
    public Add(@JsonProperty(value = "in", required = true) TemplateLiteral in,
               @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object value = in.get(context);
        context.addField(out, value);
        return EMPTY;
    }
}