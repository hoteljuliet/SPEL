package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Now extends Step {
    private String dest;
    private String to;

    private String zone;
    private DateTimeFormatter toFormatter;

    @JsonCreator
    public Now(@JsonProperty(value = "dest", required = true) String dest,
               @JsonProperty(value = "to", required = true) String to,
               @JsonProperty(value = "zone", required = true) String zone) {
        this.dest = dest;
        this.to = to;
        this.zone = zone;
        if (to.equalsIgnoreCase("unix_ms") || to.equalsIgnoreCase("unix_s")) {
            toFormatter = null;
        }
        else {
            toFormatter = DateTimeFormatter.ofPattern(to);
        }
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        try {
            ZonedDateTime original = ZonedDateTime.now(ZoneId.of(zone));
            if (to.equalsIgnoreCase("unix_ms")) {
                String reformatted = String.valueOf(original.toInstant().toEpochMilli());
                context.addField(dest, reformatted);
            }
            else if (to.equalsIgnoreCase("unix_s")) {
                String reformatted = String.valueOf(original.toInstant().getEpochSecond());
                context.addField(dest, reformatted);
            }
            else {
                String reformatted = original.format(toFormatter);
                context.addField(dest, reformatted);
            }
            success.increment();
        } catch (Exception ex) {
            exceptionThrown.increment();
        }
        return COMMAND_NEITHER;
    }
}