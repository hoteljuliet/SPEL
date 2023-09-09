package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.Context;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Date extends Step {
    private String source;
    private String dest;

    private DateTimeFormatter fromFormatter;
    private DateTimeFormatter toFormatter;

    @JsonCreator
    public Date(@JsonProperty(value = "source", required = true) String source,
                @JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "from", required = true) String from,
                @JsonProperty(value = "to", required = true) String to) {
        this.source = source;
        this.dest = dest;
        fromFormatter = DateTimeFormatter.ofPattern(from);
        toFormatter = DateTimeFormatter.ofPattern(to);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        try {
            if (context.hasField(source)) {
                String value = context.getField(source);
                ZonedDateTime original = ZonedDateTime.parse(value, fromFormatter);
                String reformatted = original.format(toFormatter);
                context.addField(dest, reformatted);
                success.increment();
            }
            else {
                missing.increment();
            }
        } catch (Exception ex) {
            exceptionThrown.increment();
        }
        return COMMAND_NEITHER;
    }
}