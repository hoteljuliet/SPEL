package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.FieldType;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;

/**
 * Convert a field's type
 */
@Step(tag = "cast")
public class Cast extends StepStatement implements Serializable {

    private final String value;
    private final FieldType fieldType;

    @JsonCreator
    public Cast(@JsonProperty(value = "value", required = true) String value,
                @JsonProperty(value = "to", required = true) FieldType fieldType) {
        this.value = value;
        this.fieldType = fieldType;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object original = context.getField(value);
        Object afterCast = fieldType.convertFrom(original);
        if (null == afterCast) {
            softFailure();
        }
        else {
            context.replaceFieldValue(value, afterCast);
        }
        return EMPTY;
    }
}
