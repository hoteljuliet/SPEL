package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;

public class HasField extends PredicateStep implements Serializable {
    private String source;

    @JsonCreator
    public HasField(@JsonProperty(value = "source", required = true) String source) {
        super();
        this.source = source;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            return Step.COMMAND_TRUE;
        }
        else {
            return Step.COMMAND_FALSE;
        }
    }
}
