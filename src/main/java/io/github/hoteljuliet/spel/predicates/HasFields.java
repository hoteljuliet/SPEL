package io.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepPredicate;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "has-fields")
public class HasFields extends StepPredicate implements Serializable {
    private List<String> paths;

    @JsonCreator
    public HasFields(@JsonProperty(value = "paths", required = true) List<String> paths) {
        super();
        this.paths = paths;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Optional<Boolean> retVal = TRUE;
        for (String path : paths) {
            if (!context.hasField(path)) {
                retVal = FALSE;
                break;
            }
        }
        return retVal;
    }
}