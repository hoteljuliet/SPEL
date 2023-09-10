package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AsList extends StatementStep {

    private List<String> sources;
    private String dest;
    @JsonCreator
    public AsList(@JsonProperty(value = "sources", required = true) java.util.List<String> sources,
                  @JsonProperty(value = "dest", required = true) String dest) {
        this.sources = sources;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        List<Object> list = new ArrayList<>();
        for (String source : sources) {
            if (context.hasField(source)) {
                Object value = context.getField(source);
                list.add(value);
            }
            else {
                context.missingField(name);
            }
        }
        context.addField(dest, list);
        return COMMAND_NEITHER;
    }
}