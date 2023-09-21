package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.FieldType;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

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
