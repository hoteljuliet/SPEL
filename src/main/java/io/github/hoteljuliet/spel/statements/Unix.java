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

    private final ZoneId toZone;

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
                @JsonProperty(value = "out", required = true) String out,
                @JsonProperty(value = "from", required = true) String from,
                @JsonProperty(value = "to", required = true) String to,
                @JsonProperty(value = "fromZone", required = true) String fromZone,
                @JsonProperty(value = "fromZone", required = false) String toZone) {
        super();
        this.in = in;
        this.out = out;
        this.from = from;
        this.to = to;
        this.fromZone = ZoneId.of(fromZone);
        this.toZone = (null == toZone) ? null : ZoneId.of(toZone);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == fromFormatter) fromFormatter = DateFormats.of(from).withZone(fromZone);
        if (null == toFormatter) toFormatter = (null == toZone) ? DateFormats.of(to) : DateFormats.of(to).withZone(toZone);
        Long value = context.getField(in);

        if (from.equalsIgnoreCase("UNIX_S")) {
            value = value * 1000;
        }

        Instant instant = Instant.ofEpochMilli(value);
        ZonedDateTime original = ZonedDateTime.ofInstant(instant, fromZone);
        String reformatted = original.format(toFormatter);
        context.addField(out, reformatted);
        return EMPTY;
    }
}