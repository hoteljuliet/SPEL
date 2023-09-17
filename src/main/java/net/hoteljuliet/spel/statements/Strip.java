package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "strip")
public class Strip extends StepStatement implements Serializable {
    private String source;

    @JsonCreator
    public Strip(@JsonProperty(value = "source", required = true) String source) {
        super();
        this.source = source;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            String value = context.getField(source);
            String stripped = value.replaceAll("\\s+","");
            context.replaceFieldValue(source, stripped);
        }
        else {
            missingField();
        }
        return StepBase.NEITHER;
    }
}

