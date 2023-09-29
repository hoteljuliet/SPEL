package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Step(tag = "as-list")
public class AsList extends StepStatement implements Serializable {

    private List<TemplateLiteral> in;
    private String out;

    /**
     * Create a list from multiple values and add the List to the context
     * @param in a List of {@link io.github.hoteljuliet.spel.mustache.TemplateLiteral} to create a list from.
     * @param out the path into the context where a List will be created
     */
    @JsonCreator
    public AsList(@JsonProperty(value = "in", required = true) List<TemplateLiteral> in,
                  @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        List<Object> addedList = new ArrayList<>();
        for (TemplateLiteral templateLiteral : in) {
            Object value = templateLiteral.get(context);
            addedList.add(value);
        }
        context.addField(out, addedList);
        return EMPTY;
    }
}