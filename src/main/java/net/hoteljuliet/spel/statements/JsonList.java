package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JsonList extends Step {

    private String source;
    private String dest;
    private ObjectMapper objectMapper;

    @JsonCreator
    public JsonList(@JsonProperty(value = "source", required = true) String source,
                    @JsonProperty(value = "dest", required = true) String dest) {
        this.source = source;
        this.dest = dest;
        objectMapper = new ObjectMapper();
        // TODO allow mapper config changes?
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        try {
            if (context.hasField(source)) {
                String value = context.getField(source);
                Object mappedValue = objectMapper.readValue(value, List.class);
                context.addField(dest, mappedValue);
                success.increment();
            }
            else {
                missing.increment();
            }
        }
        catch(Exception ex) {
            handleException(ex);
        }
        return COMMAND_NEITHER;
    }
}