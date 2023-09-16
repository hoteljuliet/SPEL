package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.BaseStep;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.FieldType;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "has-type")
public class HasType extends PredicateBaseStep implements Serializable {
    private final String source;
    private final FieldType type;

    @JsonCreator
    public HasType(@JsonProperty(value = "source", required = true) String source,
                   @JsonProperty(value = "type", required = true) FieldType type) {
        super();
        this.source = source;
        this.type = type;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            Object fieldValue = context.get(source);
            return type.isInstance(fieldValue) ? BaseStep.TRUE : BaseStep.FALSE;
        }
        else {
            return BaseStep.FALSE;
        }
    }
}

