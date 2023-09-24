package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.*;
import net.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "add")
public class Add extends StepStatement implements Serializable {
    private final TemplateLiteral templateLiteral;
    private final String dest;

    @JsonCreator
    public Add(@JsonProperty(value = "value", required = true) TemplateLiteral templateLiteral,
               @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.templateLiteral = templateLiteral;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object value = templateLiteral.get(context);
        context.addField(dest, value);
        return EMPTY;
    }
}