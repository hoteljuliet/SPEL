package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public class Translate extends StatementStep implements Serializable {
    private String source;
    private String defaultValue;
    private Map<String, String> dict;

    @JsonCreator
    public Translate(@JsonProperty(value = "source", required = true) String source,
                     @JsonProperty(value = "default", required = true) String defaultValue,
                     @JsonProperty(value = "dict", required = true) Map<String, String> dict) {
        super();
        this.source = source;
        this.defaultValue = defaultValue;
        this.dict = dict;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (context.hasField(source)) {
            String value = context.getField(source);
            String translation = dict.get(value);
            if (translation != null) {
                context.replaceFieldValue(source, translation);
            } else {
                context.replaceFieldValue(source, defaultValue);
            }
        }
        else {
            missingField.increment();
        }
        return COMMAND_NEITHER;
    }
}
