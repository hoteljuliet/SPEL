package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "in")
public class In extends PredicateBaseStep implements Serializable {

    private String source;
    private List<Object> list;

    @JsonCreator
    public In(@JsonProperty(value = "source", required = true) String source,
              @JsonProperty(value = "list", required = true) List<Object> list) {
        super();
        this.source = source;
        this.list = list;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Optional<Boolean> retVal;

        if (context.hasField(source)) {
            Object value = context.getField(source);

            if (list.contains(value)) {
                retVal = TRUE;
            }
            else {
                retVal = FALSE;
            }
        } else {
            missingField.increment();
            retVal = FALSE;
        }
        return retVal;
    }
}