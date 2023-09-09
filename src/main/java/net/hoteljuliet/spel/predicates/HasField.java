package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.Context;

import java.util.Optional;

public class HasField extends PredicateStep {
    private String source;
    @JsonCreator
    public HasField(@JsonProperty(value = "source", required = true) String source) {
        this.source = source;
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            evalTrue.increment();
            return Command.COMMAND_TRUE;
        }
        else {
            evalFalse.increment();
            return Command.COMMAND_FALSE;
        }
    }
}
