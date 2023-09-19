package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Lookup data against a Dictoinary that is provided via config
 */
@Step(tag = "lookup-i")
public class LookUpImmediate extends StepStatement implements Serializable {
    private final List<String> sources;
    private final List<String> dests;
    private final String defaultValue;
    private final Map<Object, Object> dict;
    @JsonCreator
    public LookUpImmediate(@JsonProperty(value = "sources", required = true) List<String> sources,
                           @JsonProperty(value = "dests", required = true) List<String> dests,
                           @JsonProperty(value = "default", required = true) String defaultValue,
                           @JsonProperty(value = "dict", required = true) Map<Object, Object> dict) {
        super();
        this.sources = sources;
        this.dests = dests;
        this.defaultValue = defaultValue;
        this.dict = dict;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        for (int i = 0; i < sources.size() && i < dests.size(); i++) {
            Object value = context.getField(sources.get(i));
            if (dict.containsKey(value)) {
                Object lookup = dict.get(value);
                context.addField(dests.get(i), lookup);
            } else {
                context.replaceFieldValue(dests.get(i), defaultValue);
            }
        }
        return EMPTY;
    }
}