package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Step(tag = "key-value")
public class KeyValue extends StepStatement implements Serializable {

    private String in;
    private String delimiter;
    private String separator;
    private String out;

    /**
     *
     * @param in the path in the Context to a key-value String
     * @param delimiter the character the K/V pairs are delimited by
     * @param separator the separator each K/V is separated by
     * @param out a path in the Context to where a Map of the K/V pairs will be placed
     */
    @JsonCreator
    public KeyValue(@JsonProperty(value = "in", required = true) String in,
                    @JsonProperty(value = "delimiter", required = true) String delimiter,
                    @JsonProperty(value = "separator", required = true) String separator,
                    @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.delimiter = delimiter;
        this.out = out;
        this.separator = separator;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Map<String, String> result = new HashMap<>();
        String value = context.getField(in);
        String[] kvPairs = value.split(delimiter);
        for (String pair : kvPairs) {
            String[] parts = pair.split(separator);
            result.put(parts[0], parts[1]);
        }
        context.addField(out, result);
        return EMPTY;
    }
}
