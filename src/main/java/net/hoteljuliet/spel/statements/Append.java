package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;

import java.util.List;
import java.util.Optional;
public class Append extends StatementStep {
    private String source;
    private String dest;
    @JsonCreator
    public Append(@JsonProperty(value = "source", required = true) String source,
                  @JsonProperty(value = "dest", required = true) String dest) {
        this.source = source;
        this.dest = dest;
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source) && context.hasField(dest)) {
            Object value = context.getField(source);
            List target = context.getField(dest);
            target.add(value);
            context.replaceFieldValue(dest, target);
        }
        else {
            context.softFailure(name);
        }
        return Command.COMMAND_NEITHER;
    }
}
