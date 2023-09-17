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

    private final String source;
    private final FieldType fieldType;

    @JsonCreator
    public Cast(@JsonProperty(value = "source", required = true) String source,
                @JsonProperty(value = "to", required = true) FieldType fieldType) {
        this.source = source;
        this.fieldType = fieldType;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            Object original = context.getField(source);
            Object afterCast = fieldType.convertFrom(original);

            if (afterCast != null) {
                context.replaceFieldValue(source, afterCast);
            }
            else {
                softFailure();
            }
        }
        else {
            missingField();
        }
        return NEITHER;
    }
}
