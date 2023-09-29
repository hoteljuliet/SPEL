package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "remove")
public class Remove extends StepStatement implements Serializable {
    private List<String> in;

    @JsonCreator
    public Remove(@JsonProperty(value = "in", required = true) List<String> in) {
        super();
        this.in = in;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        for (String path : in) {
            context.removeField(path);
        }
        return EMPTY;
    }
}


