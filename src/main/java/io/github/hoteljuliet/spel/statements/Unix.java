package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.DateFormats;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Step(tag = "unix")
public class Unix extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final String from;
    private final String to;
    private final ZoneId fromZone;
    private final Optional<ZoneId> toZone;
    private transient DateTimeFormatter fromFormatter;
    private transient DateTimeFormatter toFormatter;


    /**
     * Reformats a long unix timestamp (seconds or millis) to a String
     * @param in a path to the date string to reformat
     * @param out the path in the context where the new date string will be placed
     * @param from the original date format, See {@link java.time.format.DateTimeFormatter}
     * @param to the desired output date format, See {@link java.time.format.DateTimeFormatter}
     */
    @JsonCreator
    public Unix(@JsonProperty(value = "in", required = true) String in,
                @JsonProperty(value = "from", required = true) String from,
                @JsonProperty(value = "to", required = true) String to,
                @JsonProperty(value = "fromZone", required = true) String fromZone,
                @JsonProperty(value = "fromZone", required = false) String toZone,
                @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.from = from;
        this.to = to;
        this.fromZone = ZoneId.of(fromZone);
        this.toZone = (null == toZone) ? Optional.empty() : Optional.of(ZoneId.of(toZone));
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == fromFormatter) fromFormatter = DateFormats.of(from).withZone(fromZone);
        if (null == toFormatter) toFormatter = (toZone.isEmpty()) ? DateFormats.of(to) : DateFormats.of(to).withZone(toZone.get());
        Object value = context.getField(in);
        Long longValue = 0l;

        if (value instanceof Integer) {
            longValue = ((Integer)value).longValue();
        }
        else if (value instanceof Long) {
            longValue = (Long)value;
        }
        else {
            softFailure();
        }

        if (from.equalsIgnoreCase("UNIX_S")) {
            longValue = longValue * 1000;
        }

        Instant instant = Instant.ofEpochMilli(longValue);
        ZonedDateTime original = ZonedDateTime.ofInstant(instant, fromZone);
        String reformatted = original.format(toFormatter);
        context.addField(out, reformatted);
        return EMPTY;
    }
}