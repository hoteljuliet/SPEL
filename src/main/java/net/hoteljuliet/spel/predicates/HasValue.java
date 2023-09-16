package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.BaseStep;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "has-value")
public class HasValue extends PredicateBaseStep implements Serializable {
    private final String source;
    private final Object value;

    @JsonCreator
    public HasValue(@JsonProperty(value = "source", required = true) String source,
                    @JsonProperty(value = "value", required = true) Object value) {
        super();
        this.source = source;
        this.value = value;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            Object fieldValue = context.get(source);
            return fieldValue.equals(value) ? BaseStep.TRUE : BaseStep.FALSE;
        }
        else {
            return BaseStep.FALSE;
        }
    }
}
