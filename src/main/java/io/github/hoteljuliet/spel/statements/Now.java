package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Step(tag = "now")
public class Now extends StepStatement implements Serializable {

    public static final String UNIX_MILLIS = "unix_ms";
    public static final String UNIX_SECONDS= "unix_s";
    private final String out;
    private final String format;
    private final ZoneId zone;
    private transient Optional<DateTimeFormatter> toFormatter;

    /**
     *
     * @param out the path in the Context where a new date String will be placed
     * @param format the format of the output, can be "unix_ms", "unix_s", or See {@link java.time.format.DateTimeFormatter}
     * @param zone the time zone, See {@link java.time.ZoneId}
     */
    @JsonCreator
    public Now(@JsonProperty(value = "out", required = true) String out,
               @JsonProperty(value = "format", required = true) String format,
               @JsonProperty(value = "zone", required = true) String zone) {
        super();
        this.out = out;
        this.format = format;
        this.zone = ZoneId.of(zone);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (null == toFormatter) {
            if (format.equalsIgnoreCase(UNIX_MILLIS) || format.equalsIgnoreCase(UNIX_SECONDS)) {
                toFormatter = Optional.empty();
            }
            else {
                toFormatter = Optional.of(DateTimeFormatter.ofPattern(format));
            }
        }

        ZonedDateTime original = ZonedDateTime.now(this.zone);
        if (format.equalsIgnoreCase(UNIX_MILLIS)) {
            String reformatted = String.valueOf(original.toInstant().toEpochMilli());
            context.addField(out, reformatted);
        }
        else if (format.equalsIgnoreCase(UNIX_SECONDS)) {
            String reformatted = String.valueOf(original.toInstant().getEpochSecond());
            context.addField(out, reformatted);
        }
        else {
            String reformatted = original.format(toFormatter.get());
            context.addField(out, reformatted);
        }
        return EMPTY;
    }
}