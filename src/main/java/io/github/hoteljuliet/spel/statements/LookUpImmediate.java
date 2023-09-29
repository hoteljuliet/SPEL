package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Lookup data against a Dictoinary that is provided via config
 */

// TODO: consolidate with TemplateLiteral
@Step(tag = "lookup-i")
public class LookUpImmediate extends StepStatement implements Serializable {
    private final List<String> in;
    private final List<String> out;
    private final String defaultValue;
    private final Map<Object, Object> dict;
    @JsonCreator
    public LookUpImmediate(@JsonProperty(value = "in", required = true) List<String> in,
                           @JsonProperty(value = "out", required = true) List<String> out,
                           @JsonProperty(value = "default", required = true) String defaultValue,
                           @JsonProperty(value = "dict", required = true) Map<Object, Object> dict) {
        super();
        this.in = in;
        this.out = out;
        this.defaultValue = defaultValue;
        this.dict = dict;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        for (int i = 0; i < in.size() && i < out.size(); i++) {
            Object value = context.getField(in.get(i));
            if (dict.containsKey(value)) {
                Object lookup = dict.get(value);
                context.addField(out.get(i), lookup);
            } else {
                context.addField(out.get(i), defaultValue);
            }
        }
        return EMPTY;
    }
}