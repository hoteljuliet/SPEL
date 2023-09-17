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

    private final List<String> sources;
    private final String dest;
    @JsonCreator
    public Concat(@JsonProperty(value = "sources", required = true) List<String> sources,
                  @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.sources = sources;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        for (String source : sources) {
            if (context.hasField(source)) {
                String value = context.getField(source);
                stringBuilder.append(value);
            }
            else {
                missingField();
            }
        }
        context.addField(dest, stringBuilder.toString());
        return NEITHER;
    }
}

