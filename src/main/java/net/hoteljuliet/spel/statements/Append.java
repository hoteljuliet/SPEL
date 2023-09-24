package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Step(tag = "append")
public class Append extends StepStatement implements Serializable {
    private final TemplateLiteral value;
    private final String list;

    @JsonCreator
    public Append(@JsonProperty(value = "value", required = true) TemplateLiteral value,
                  @JsonProperty(value = "list", required = true) String list) {
        super();
        this.value = value;
        this.list = list;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object v = value.get(context);
        if (context.hasField(list)) {
            List<Object> l = context.getField(list);
            l.add(value);
            context.replaceFieldValue(list, l);
        }
        else {
            List<Object> l = new ArrayList<>();
            l.add(value);
            context.addField(list, l);
        }
        return StepBase.EMPTY;
    }
}
