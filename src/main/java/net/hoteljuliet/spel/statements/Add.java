package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.*;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "add")
public class Add extends StepStatement implements Serializable {
    private final String dest;
    private final FieldType type;
    private final TemplateLiteral templateLiteral;

    @JsonCreator
    public Add(@JsonProperty(value = "value", required = true) TemplateLiteral templateLiteral,
               @JsonProperty(value = "dest", required = true) String dest,
               @JsonProperty(value = "type", required = false, defaultValue = "string") FieldType type) {
        super();
        this.templateLiteral = templateLiteral;
        this.dest = dest;
        this.type = type;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object value = templateLiteral.get(context);
        context.addField(dest, type.convertFrom(value));
        return StepBase.EMPTY;
    }
}