package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

@Step(tag = "translate")
public class Translate extends StepStatement implements Serializable {
    private String value;
    private String defaultValue;
    private Map<String, String> dict;

    @JsonCreator
    public Translate(@JsonProperty(value = "value", required = true) String value,
                     @JsonProperty(value = "default", required = true) String defaultValue,
                     @JsonProperty(value = "dict", required = true) Map<String, String> dict) {
        super();
        this.value = value;
        this.defaultValue = defaultValue;
        this.dict = dict;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String v = context.getField(value);
        String translation = dict.get(v);
        if (translation != null) {
            context.replaceFieldValue(value, translation);
        } else {
            context.replaceFieldValue(value, defaultValue);
        }
        return EMPTY;
    }
}
