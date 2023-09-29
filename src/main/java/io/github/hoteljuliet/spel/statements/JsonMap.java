package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

@Step(tag = "json-map")
public class JsonMap extends StepStatement implements Serializable {

    private String in;
    private String out;
    private ObjectMapper objectMapper;

    /**
     * Given a JSON Map String, map it to a Map and add it to the context.
     * @param in a path to a JSON String in the context
     * @param out a path in the Context to where the mapped Map will be placed
     */
    @JsonCreator
    public JsonMap(@JsonProperty(value = "in", required = true) String in,
                   @JsonProperty(value = "out", required = true) String out) {
        this.in = in;
        this.out = out;
        objectMapper = new ObjectMapper();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(in);
        Object mappedValue = objectMapper.readValue(value, Map.class);
        context.addField(out, mappedValue);
        return EMPTY;
    }
}