package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * The inverse of remove, keep only those fields specified - remove all others from context
 */
@Step(tag = "keep-only")
public class KeepOnly extends StepStatement implements Serializable {
    private final List<String> in;

    /**
     * Remove all fields in the context, except those specified
     * @param in a list of paths in the Context to keep, all others will be removed
     */
    @JsonCreator
    public KeepOnly(@JsonProperty(value = "in", required = true) List<String> in) {
        super();
        this.in = in;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Context newContext = new Context();
        for (String value : in) {
            Object object = context.getField(value);
            newContext.addField(value, object);
        }
        context.replace(newContext.getBacking());
        return EMPTY;
    }
}

