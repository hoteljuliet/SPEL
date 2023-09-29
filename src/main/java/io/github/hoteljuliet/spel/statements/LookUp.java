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
 * Lookup data against a Dictionary that exists in the context
 */
// TODO: consolidate with TemplateLiteral
@Step(tag = "lookup")
public class LookUp extends StepStatement implements Serializable {
    private final List<String> in;
    private final List<String> out;
    private final String defaultValue;
    private final String dict;
    @JsonCreator
    public LookUp(@JsonProperty(value = "in", required = true) List<String> in,
                  @JsonProperty(value = "out", required = true) List<String> out,
                  @JsonProperty(value = "default", required = true) String defaultValue,
                  @JsonProperty(value = "dict", required = true) String dict) {
        super();
        this.in = in;
        this.out = out;
        this.defaultValue = defaultValue;
        this.dict = dict;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Map<String, Object> lookupDict = context.getField(dict);
        for (int i = 0; i < in.size() && i < out.size(); i++) {
            Object value = context.getField(in.get(i));
            if (lookupDict.containsKey(value)) {
                Object lookup = lookupDict.get(value);
                context.addField(out.get(i), lookup);
            } else {
                context.addField(out.get(i), defaultValue);
            }
        }
        return EMPTY;
    }
}