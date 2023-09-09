package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;

import java.util.Map;
import java.util.Optional;

public class Rename extends StatementStep {

    private Map<String, String> dict;
    @JsonCreator
    public Rename(@JsonProperty(value = "dict", required = true) Map<String, String> dict) {
        this.dict = dict;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        try {
            for (Map.Entry<String, String> rename : dict.entrySet()) {
                if (context.hasField(rename.getKey())) {
                    Object value = context.getField(rename.getKey());
                    context.removeField(rename.getKey());
                    context.addField(rename.getValue(), value);
                    success.increment();
                }
                else {
                    missing.increment();
                }
            }
        }
        catch(Exception ex) {
            handleException(ex);
        }
        return COMMAND_NEITHER;
    }
}
