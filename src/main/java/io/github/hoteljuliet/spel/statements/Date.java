package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.DateFormats;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Step(tag = "date")
public class Date extends StepStatement implements Serializable {
    private final String in;
    private final String from;
    private final String to;
    private final Optional<ZoneId> fromZone;
    private final Optional<ZoneId> toZone;
    private final String out;
    private transient DateTimeFormatter fromFormatter;
    private transient DateTimeFormatter toFormatter;


    /**
     * Reformats a date String into another date String
     * @param in a path to the date string to reformat
     * @param out the path in the context where the new date string will be placed
     * @param from the original date format, See {@link java.time.format.DateTimeFormatter}
     * @param to the desired output date format, See {@link java.time.format.DateTimeFormatter}
     */
    @JsonCreator
    public Date(@JsonProperty(value = "in", required = true) String in,
                @JsonProperty(value = "from", required = true) String from,
                @JsonProperty(value = "to", required = true) String to,
                @JsonProperty(value = "fromZone", required = false) String fromZone,
                @JsonProperty(value = "fromZone", required = false) String toZone,
                @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.from = from;
        this.to = to;
        this.fromZone = (null == fromZone) ? Optional.empty() : Optional.of(ZoneId.of(fromZone));
        this.toZone = (null == toZone) ? Optional.empty() : Optional.of(ZoneId.of(toZone));
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == fromFormatter) fromFormatter = (fromZone.isEmpty()) ? DateFormats.of(from) : DateFormats.of(from).withZone(fromZone.get());
        if (null == toFormatter) toFormatter = (toZone.isEmpty()) ? DateFormats.of(to) : DateFormats.of(to).withZone(toZone.get());
        String value = context.getField(in);
        ZonedDateTime original = ZonedDateTime.parse(value.toString(), fromFormatter);
        String reformatted = original.format(toFormatter);
        context.addField(out, reformatted);
        return EMPTY;
    }
}