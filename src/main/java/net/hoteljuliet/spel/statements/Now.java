package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Now extends Step {
    private String dest;
    private DateTimeFormatter toFormatter;

    @JsonCreator
    public Now(@JsonProperty(value = "dest", required = true) String dest,
               @JsonProperty(value = "to", required = true) String to) {
        this.dest = dest;
        toFormatter = DateTimeFormatter.ofPattern(to);
    }

    @Override
    public Optional<Boolean> execute(Context context) throws Exception {
        try {
            ZonedDateTime original = ZonedDateTime.now();
            String reformatted = original.format(toFormatter);
            context.addField(dest, reformatted);
            success.increment();
        } catch (Exception ex) {
            exceptionThrown.increment();
        }
        return COMMAND_NEITHER;
    }
}