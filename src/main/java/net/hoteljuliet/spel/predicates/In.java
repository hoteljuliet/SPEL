package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepPredicate;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.TemplateLiteral;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "in")
public class In extends StepPredicate implements Serializable {

    private TemplateLiteral value;
    private TemplateLiteral list;

    @JsonCreator
    public In(@JsonProperty(value = "value", required = true) TemplateLiteral value,
              @JsonProperty(value = "list", required = true) TemplateLiteral list) {
        super();
        this.value = value;
        this.list = list;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object v = value.get(context);
        List l = list.get(context);
        return l.contains(v) ? TRUE : FALSE;
    }
}