package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Split extends StatementStep implements Serializable {
    private final String source;
    private final String dest;
    private final String delimiter;

    @JsonCreator
    public Split(@JsonProperty(value = "source", required = true) String source,
                 @JsonProperty(value = "delimiter", required = true) String delimiter,
                 @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.delimiter = delimiter;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            String value = context.getField(source);
            String[] split = value.split(delimiter);
            List<String> output = Arrays.asList(split);
            context.replaceFieldValue(dest, output);
        }
        else {
            missingField.increment();
        }
        return Command.COMMAND_NEITHER;
    }
}
