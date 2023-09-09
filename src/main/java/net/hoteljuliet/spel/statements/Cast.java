package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.FieldType;

import java.util.Optional;

public class Cast extends StatementStep {

    private String source;
    private FieldType fieldType;

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
                context.softFailure(name);
            }
        }
        else {
            context.missingField(name);
        }

        return COMMAND_NEITHER;
    }
}
