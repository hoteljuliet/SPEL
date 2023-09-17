package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

@Step(tag = "rename")
public class Rename extends StepStatement implements Serializable {

    private Map<String, String> dict;

    @JsonCreator
    public Rename(@JsonProperty(value = "dict", required = true) Map<String, String> dict) {
        super();
        this.dict = dict;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        for (Map.Entry<String, String> rename : dict.entrySet()) {
            if (context.hasField(rename.getKey())) {
                Object value = context.getField(rename.getKey());
                context.removeField(rename.getKey());
                context.addField(rename.getValue(), value);
            }
            else {
                missingField();
            }
        }

        return NEITHER;
    }
}
