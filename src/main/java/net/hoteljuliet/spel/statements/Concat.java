package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Concat extends StatementStep implements Serializable {

    private List<String> sources;
    private String dest;
    @JsonCreator
    public Concat(@JsonProperty(value = "sources", required = true) List<String> sources,
                  @JsonProperty(value = "dest", required = true) String dest) {
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
                context.missingField(name);
            }
        }
        context.addField(dest, stringBuilder.toString());
        return COMMAND_NEITHER;
    }
}

