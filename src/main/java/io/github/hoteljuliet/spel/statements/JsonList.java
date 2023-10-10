package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Parser;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "json-list")
public class JsonList extends StepStatement implements Serializable {

    private String in;
    private String out;


    /**
     * Given a JSON List String, map it to a List and add it to the context.
     * @param in a path to a JSON String in the context
     * @param out a path in the Context to where the mapped List will be placed
     */
    @JsonCreator
    public JsonList(@JsonProperty(value = "in", required = true) String in,
                    @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(in);
        Object mappedValue = Parser.jsonMapper.readValue(value, List.class);
        context.addField(out, mappedValue);
        return EMPTY;
    }
}