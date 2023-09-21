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
 * Lookup data against a Dictoinary that exists in the context
 */
// TODO: consolidate with TemplateLiteral
@Step(tag = "lookup")
public class LookUp extends StepStatement implements Serializable {
    private final List<String> sources;
    private final List<String> dests;
    private final String defaultValue;
    private final String dict;
    @JsonCreator
    public LookUp(@JsonProperty(value = "sources", required = true) List<String> sources,
                  @JsonProperty(value = "dests", required = true) List<String> dests,
                  @JsonProperty(value = "default", required = true) String defaultValue,
                  @JsonProperty(value = "dict", required = true) String dict) {
        super();
        this.sources = sources;
        this.dests = dests;
        this.defaultValue = defaultValue;
        this.dict = dict;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Map<String, Object> lookupDict = context.getField(dict);
        for (int i = 0; i < sources.size() && i < dests.size(); i++) {
            Object value = context.getField(sources.get(i));
            if (lookupDict.containsKey(value)) {
                Object lookup = lookupDict.get(value);
                context.addField(dests.get(i), lookup);
            } else {
                context.replaceFieldValue(dests.get(i), defaultValue);
            }
        }
        return EMPTY;
    }
}