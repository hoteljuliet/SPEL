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

    private String source;
    private String dest;
    private ObjectMapper objectMapper;

    @JsonCreator
    public JsonMap(@JsonProperty(value = "source", required = true) String source,
                   @JsonProperty(value = "dest", required = true) String dest) {
        this.source = source;
        this.dest = dest;
        objectMapper = new ObjectMapper();
        // TODO allow mapper config changes?
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(source);
        Object mappedValue = objectMapper.readValue(value, Map.class);
        context.addField(dest, mappedValue);
        return EMPTY;
    }
}