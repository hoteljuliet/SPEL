package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * Lookup data against a Dictionary that exists in the context
 */
// TODO: consolidate with TemplateLiteral
@Step(tag = "lookup")
public class LookUp extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final String defaultValue;
    private final String dict;
    @JsonCreator
    public LookUp(@JsonProperty(value = "in", required = true) String in,
                  @JsonProperty(value = "out", required = true) String out,
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
        Object value = context.getField(in);
        if (lookupDict.containsKey(value)) {
            Object lookup = lookupDict.get(value);
            context.addField(out, lookup);
        } else {
            context.addField(out, defaultValue);
        }
        return EMPTY;
    }
}