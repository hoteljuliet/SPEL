package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.*;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "has-type")
public class HasType extends StepPredicate implements Serializable {
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
            return type.isInstance(fieldValue) ? StepBase.TRUE : StepBase.FALSE;
        }
        else {
            return StepBase.FALSE;
        }
    }
}

