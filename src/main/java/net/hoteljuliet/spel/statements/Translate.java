package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.Context;

import java.util.Map;
import java.util.Optional;
public class Translate extends Step {
    private String source;
    private String defaultValue;
    private Map<String, String> dict;
    @JsonCreator
    public Translate(@JsonProperty(value = "source", required = true) String source,
                     @JsonProperty(value = "default", required = true) String defaultValue,
                     @JsonProperty(value = "dict", required = true) Map<String, String> dict) {
        this.source = source;
        this.defaultValue = defaultValue;
        this.dict = dict;
    }

    @Override
    public Optional<Boolean> execute(Context context) throws Exception {
        try {
            if (context.hasField(source)) {
                String value = context.getField(source);
                if (dict.containsKey(value)) {
                    String translation = dict.get(value);
                    context.replaceFieldValue(source, translation);
                } else {
                    context.replaceFieldValue(source, defaultValue);
                }
                success.increment();
            }
            else {
                missing.increment();
            }
        }
        catch(Exception ex) {
            otherFailure.increment();
        }
        return COMMAND_NEITHER;
    }
}
