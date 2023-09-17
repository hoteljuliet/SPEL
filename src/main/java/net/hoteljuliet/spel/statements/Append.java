package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Step(tag = "append")
public class Append extends StepStatement implements Serializable {
    private final String source;
    private final String dest;

    @JsonCreator
    public Append(@JsonProperty(value = "source", required = true) String source,
                  @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            Object value = context.getField(source);
            if (context.hasField(dest)) {
                List<Object> target = context.getField(dest);
                target.add(value);
                context.replaceFieldValue(dest, target);
            }
            else if (context.hasField(source)) {
                List<Object> target = new ArrayList<>();
                target.add(value);
                context.addField(dest, target);
            }
        }
        else {
            softFailure();
        }
        return StepBase.NEITHER;
    }
}
