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

    private final String in;
    private final FieldType fieldType;

    /**
     * Cast a value from one type to another, useful for when you receive "numerical strings" like "10.05".
     * @param in a path to the value in the context
     * @param fieldType the type to cast to, must be one of {@link io.github.hoteljuliet.spel.FieldType}
     */
    @JsonCreator
    public Cast(@JsonProperty(value = "in", required = true) String in,
                @JsonProperty(value = "to", required = true) FieldType fieldType) {
        this.in = in;
        this.fieldType = fieldType;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object original = context.getField(in);
        Object afterCast = fieldType.convertFrom(original);
        if (null == afterCast) {
            softFailure();
        }
        else {
            context.replaceFieldValue(in, afterCast);
        }
        return EMPTY;
    }
}
