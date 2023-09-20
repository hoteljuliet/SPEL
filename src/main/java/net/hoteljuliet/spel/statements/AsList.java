package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.TemplateLiteral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Step(tag = "as-list")
public class AsList extends StepStatement implements Serializable {

    private List<TemplateLiteral> values;
    private String list;

    @JsonCreator
    public AsList(@JsonProperty(value = "values", required = true) List<TemplateLiteral> values,
                  @JsonProperty(value = "list", required = true) String list) {
        super();
        this.values = values;
        this.list = list;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        List<Object> l = new ArrayList<>();
        for (TemplateLiteral t : values) {
            Object value = t.get(context);
            l.add(value);
        }
        context.addField(list, l);
        return EMPTY;
    }
}