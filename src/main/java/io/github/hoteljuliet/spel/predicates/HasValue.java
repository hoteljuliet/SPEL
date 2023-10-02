package io.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepPredicate;
import io.github.hoteljuliet.spel.StepBase;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "has-value")
public class HasValue extends StepPredicate implements Serializable {
    private final String in;
    private final List<Object> values;

    @JsonCreator
    public HasValue(@JsonProperty(value = "in", required = true) String in,
                    @JsonProperty(value = "values", required = true) List<Object> values) {
        super();
        this.in = in;
        this.values = values;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object fieldValue = context.get(in);
        return values.contains(fieldValue) ? StepBase.TRUE : StepBase.FALSE;
    }
}
