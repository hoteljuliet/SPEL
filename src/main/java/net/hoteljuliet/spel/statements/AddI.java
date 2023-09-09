package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.util.Optional;

public class AddI extends StatementStep {

    private String dest;
    private Object value;

    @JsonCreator
    public AddI(@JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "value", required = true) Object value) {
        this.dest = dest;
        this.value = value;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        try {
            context.addField(dest, value);
            success.increment();
        }
        catch(Exception ex) {
            otherFailure.increment();
        }
        return COMMAND_NEITHER;
    }
}