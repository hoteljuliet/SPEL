package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Step(tag = "as-list")
public class AsList extends StepStatement implements Serializable {

    private List<String> sources;
    private String dest;
    @JsonCreator
    public AsList(@JsonProperty(value = "sources", required = true) List<String> sources,
                  @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.sources = sources;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        List<Object> list = new ArrayList<>();
        for (String source : sources) {
            Object value = context.getField(source);
            list.add(value);
        }
        context.addField(dest, list);
        return EMPTY;
    }
}