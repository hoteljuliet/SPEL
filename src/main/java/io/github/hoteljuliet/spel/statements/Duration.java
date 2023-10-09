package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.DateFormats;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Gives the paths to 2 dates, output the duration in the unit provided to the destination provided
 */
@Step(tag = "duration")
public class Duration extends StepStatement implements Serializable {
    private final String start;
    private final String end;
    private final String format;
    private final ChronoUnit unit;
    private final String out;
    private transient DateTimeFormatter dateTimeFormatter;

    /**
     * Gives the paths to 2 dates, output the duration in the unit provided to the destination provided
     * @param start - a path to the date string
     * @param end - a path to a date string
     * @param format - the format of the date strings, See {@link java.time.format.DateTimeFormatter}
     * @param unit - the unit of the time duration, See {@link java.time.temporal.ChronoUnit}
     * @param out - the path in the context where the calculated duration will be placed
     */
    @JsonCreator
    public Duration(@JsonProperty(value = "start", required = true) String start,
                    @JsonProperty(value = "end", required = true) String end,
                    @JsonProperty(value = "format", required = true) String format,
                    @JsonProperty(value = "unit", required = true) String unit,
                    @JsonProperty(value = "out", required = true) String out) {
        super();
        this.start = start;
        this.end = end;
        this.format = format;
        this.unit = ChronoUnit.valueOf(unit.toUpperCase());
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == dateTimeFormatter) dateTimeFormatter = DateFormats.of(format);

        ZonedDateTime startTime = ZonedDateTime.parse(context.getField(start), dateTimeFormatter);
        ZonedDateTime endTime = ZonedDateTime.parse(context.getField(end), dateTimeFormatter);
        Long duration = java.lang.Math.abs(unit.between(startTime, endTime));
        context.addField(out, duration);
        return EMPTY;
    }
}