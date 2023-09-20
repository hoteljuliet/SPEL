package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "concat")
public class Concat extends StepStatement implements Serializable {

    private final List<String> values;
    private final String dest;
    @JsonCreator
    public Concat(@JsonProperty(value = "values", required = true) List<String> values,
                  @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.values = values;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : values) {
            String v = context.getField(value);
            stringBuilder.append(value);
        }
        context.addField(dest, stringBuilder.toString());
        return EMPTY;
    }
}

