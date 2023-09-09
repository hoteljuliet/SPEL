package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;

import java.util.List;
import java.util.Optional;

public class In extends PredicateStep {

    private String source;
    private List<Object> list;

    @JsonCreator
    public In(@JsonProperty(value = "source", required = true) String source,
              @JsonProperty(value = "list", required = true) List<Object> list) {
        this.source = source;
        this.list = list;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Optional<Boolean> retVal;

        try {
            if (context.hasField(source)) {
                Object value = context.getField(source);

                if (list.contains(value)) {
                    evalTrue.increment();
                    retVal = COMMAND_TRUE;
                }
                else {
                    evalFalse.increment();
                    retVal = COMMAND_FALSE;
                }
            } else {
                missing.increment();
                retVal = COMMAND_FALSE;
            }
        } catch (Exception ex) {
            handleException(ex);
            retVal = COMMAND_FALSE;
        }
        return retVal;
    }
}