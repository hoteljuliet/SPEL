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
    private String value;

    @JsonCreator
    public Strip(@JsonProperty(value = "value", required = true) String value) {
        super();
        this.value = value;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String v = context.getField(value);
        String stripped = v.replaceAll("\\s+","");
        context.replaceFieldValue(value, stripped);
        return StepBase.EMPTY;
    }
}

