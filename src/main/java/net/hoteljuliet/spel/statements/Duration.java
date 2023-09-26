package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Gives the paths to 2 dates, output the duration in the unit provided to the destination provided
 */
@Step(tag = "duration")
public class Duration extends StepStatement implements Serializable {
    private final String from;
    private final String to;
    private final String pattern;
    private final String unit;
    private final String dest;
    private transient DateTimeFormatter dateTimeFormatter;

    @JsonCreator
    public Duration(@JsonProperty(value = "from", required = true) String from,
                    @JsonProperty(value = "to", required = true) String to,
                    @JsonProperty(value = "pattern", required = true) String pattern,
                    @JsonProperty(value = "unit", required = true) String unit,
                    @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.from = from;
        this.to = to;
        this.pattern = pattern;
        this.unit = unit;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == dateTimeFormatter) dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);

        ZonedDateTime start = ZonedDateTime.parse(context.getField(from), dateTimeFormatter);
        ZonedDateTime end = ZonedDateTime.parse(context.getField(to), dateTimeFormatter);
        Long duration = ChronoUnit.valueOf(unit).between(start, end);
        context.addField(dest, duration);
        return EMPTY;
    }
}