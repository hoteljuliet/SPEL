package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Step(tag = "now")
public class Now extends StepStatement implements Serializable {
    private final String dest;
    private final String to;

    private final String zone;
    private transient Optional<DateTimeFormatter> toFormatter;

    @JsonCreator
    public Now(@JsonProperty(value = "dest", required = true) String dest,
               @JsonProperty(value = "to", required = true) String to,
               @JsonProperty(value = "zone", required = true) String zone) {
        super();
        this.dest = dest;
        this.to = to;
        this.zone = zone;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (null == toFormatter) {
            if (to.equalsIgnoreCase("unix_ms") || to.equalsIgnoreCase("unix_s")) {
                toFormatter = Optional.empty();
            }
            else {
                toFormatter = Optional.of(DateTimeFormatter.ofPattern(to));
            }
        }

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
            String reformatted = original.format(toFormatter.get());
            context.addField(dest, reformatted);
        }
        return EMPTY;
    }
}