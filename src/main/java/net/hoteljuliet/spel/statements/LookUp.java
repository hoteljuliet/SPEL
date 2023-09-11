package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LookUp extends StatementStep implements Serializable {
    private final List<String> sources;
    private final List<String> dests;
    private final String defaultValue;
    private final Map<Object, Object> dict;
    @JsonCreator
    public LookUp(@JsonProperty(value = "sources", required = true) List<String> sources,
                  @JsonProperty(value = "dests", required = true) List<String> dests,
                  @JsonProperty(value = "default", required = true) String defaultValue,
                  @JsonProperty(value = "dict", required = true) Map<Object, Object> dict) {
        this.sources = sources;
        this.dests = dests;
        this.defaultValue = defaultValue;
        this.dict = dict;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        for (int i = 0; i < sources.size() && i < dests.size(); i++) {
            if (context.hasField(sources.get(i))) {
                Object value = context.getField(sources.get(i));
                if (dict.containsKey(value)) {
                    Object lookup = dict.get(value);
                    context.addField(dests.get(i), lookup);
                } else {
                    context.replaceFieldValue(dests.get(i), defaultValue);
                }
            }
            else {
                context.missingField(name);
            }
        }

        return COMMAND_NEITHER;
    }
}