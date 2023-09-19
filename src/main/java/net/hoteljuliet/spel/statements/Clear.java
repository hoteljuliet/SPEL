package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "clear")
public class Clear extends StepStatement implements Serializable {
    private List<String> sources;

    @JsonCreator
    public Clear(@JsonProperty(value = "sources", required = true) List<String> sources) {
        super();
        this.sources = sources;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        for (String source : sources) {
            StepBase step = (StepBase) context.get(source);
            step.clear();
        }
        return EMPTY;
    }
}

