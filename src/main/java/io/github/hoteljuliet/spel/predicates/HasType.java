package io.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.*;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "has-type")
public class HasType extends StepPredicate implements Serializable {
    private final String path;
    private final FieldType type;

    @JsonCreator
    public HasType(@JsonProperty(value = "path", required = true) String path,
                   @JsonProperty(value = "type", required = true) FieldType type) {
        super();
        this.path = path;
        this.type = type;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(path)) {
            Object fieldValue = context.get(path);
            return type.isInstance(fieldValue) ? StepBase.TRUE : StepBase.FALSE;
        }
        else {
            softFailure();
            return StepBase.FALSE;
        }
    }
}

