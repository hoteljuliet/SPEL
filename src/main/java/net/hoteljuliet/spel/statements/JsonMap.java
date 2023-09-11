package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public class JsonMap extends StatementStep implements Serializable {

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

        if (context.hasField(source)) {
            String value = context.getField(source);
            Object mappedValue = objectMapper.readValue(value, Map.class);
            context.addField(dest, mappedValue);
        }
        else {
            context.missingField(name);
        }

        return COMMAND_NEITHER;
    }
}