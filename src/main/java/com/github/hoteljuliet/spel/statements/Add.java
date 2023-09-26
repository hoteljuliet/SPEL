package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepStatement;
import com.github.hoteljuliet.spel.*;
import com.github.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.Optional;

/**
 * Add a field, either a literal or from a template {{fieldX}} - but just a single value.
 * See render for full mustache support.
 */
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