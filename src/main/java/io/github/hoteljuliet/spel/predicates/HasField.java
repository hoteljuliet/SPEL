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
    private String path;

    @JsonCreator
    public HasField(@JsonProperty(value = "path", required = true) String path) {
        super();
        this.path = path;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return context.hasField(path) ? TRUE : FALSE;
    }
}
