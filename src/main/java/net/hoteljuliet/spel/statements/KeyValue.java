package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class KeyValue extends StatementStep implements Serializable {

    private String source;
    private String delimiter;
    private String separator;
    private String dest;

    @JsonCreator
    public KeyValue(@JsonProperty(value = "source", required = true) String source,
                    @JsonProperty(value = "delimiter", required = true) String delimiter,
                    @JsonProperty(value = "separator", required = true) String separator,
                    @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.delimiter = delimiter;
        this.dest = dest;
        this.separator = separator;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            Map<String, String> result = new HashMap<>();
            String value = context.getField(source);
            String[] kvPairs = value.split(delimiter);
            for (String pair : kvPairs) {
                String[] parts = pair.split(separator);
                result.put(parts[0].trim(), parts[1].trim());
            }
            context.addField(dest, result);
        }
        else {
            missingField.increment();
        }

        return COMMAND_NEITHER;
    }
}
