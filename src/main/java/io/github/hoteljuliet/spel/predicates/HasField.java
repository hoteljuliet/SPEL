package io.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.StepPredicate;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "has-field")
public class HasField extends StepPredicate implements Serializable {
    private String in;

    @JsonCreator
    public HasField(@JsonProperty(value = "in", required = true) String in) {
        super();
        this.in = in;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return context.hasField(in) ? TRUE : FALSE;
    }
}
