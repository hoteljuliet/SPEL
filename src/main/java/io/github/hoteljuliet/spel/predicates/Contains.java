package io.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepPredicate;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "contains")
public class Contains extends StepPredicate implements Serializable {

    private TemplateLiteral in;
    private TemplateLiteral values;

    @JsonCreator
    public Contains(@JsonProperty(value = "in", required = true) TemplateLiteral in,
                    @JsonProperty(value = "values", required = true) TemplateLiteral values) {
        super();
        this.in = in;
        this.values = values;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object value = in.get(context);
        List list = values.get(context);
        return list.contains(value) ? TRUE : FALSE;
    }
}