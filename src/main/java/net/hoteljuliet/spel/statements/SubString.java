package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.Optional;

public class SubString extends StatementStep implements Serializable {
    private final String source;
    private final String dest;
    private final Integer from;
    private final Integer to;

    @JsonCreator
    public SubString(@JsonProperty(value = "source", required = true) String source,
                     @JsonProperty(value = "dest", required = true) String dest,
                     @JsonProperty(value = "from", required = true) Integer from,
                     @JsonProperty(value = "to", required = true) Integer to) {
        super();
        this.source = source;
        this.dest = dest;
        this.from = from;
        this.to = to;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            String value = context.getField(source);
            String substring = value.substring(from, to);
            context.replaceFieldValue(dest, substring);
        }
        else {
            missingField.increment();
        }
        return Command.COMMAND_NEITHER;
    }
}