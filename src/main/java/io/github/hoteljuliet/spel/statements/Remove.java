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
    private List<String> sources;

    @JsonCreator
    public Remove(@JsonProperty(value = "sources", required = true) List<String> sources) {
        super();
        this.sources = sources;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        for (String path : sources) {
            context.removeField(path);
        }
        return EMPTY;
    }
}


