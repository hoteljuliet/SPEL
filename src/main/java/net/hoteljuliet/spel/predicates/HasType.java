package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.FieldType;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;

public class HasType extends PredicateStep implements Serializable {
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
            return type.isInstance(fieldValue) ? Step.COMMAND_TRUE : Step.COMMAND_FALSE;
        }
        else {
            return Step.COMMAND_FALSE;
        }
    }
}

