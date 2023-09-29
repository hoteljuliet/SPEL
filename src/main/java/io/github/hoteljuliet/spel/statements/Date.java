package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Step(tag = "date")
public class Date extends StepStatement implements Serializable {
    private String in;
    private String out;
    private String from;
    private String to;

    private transient DateTimeFormatter fromFormatter;
    private transient DateTimeFormatter toFormatter;


    /**
     * Reformats a date String
     * @param in a path to the date string to reformat
     * @param out the path in the context where the new date string will be placed
     * @param from the original date format, See {@link java.time.format.DateTimeFormatter}
     * @param to the desired output date format, See {@link java.time.format.DateTimeFormatter}
     */
    @JsonCreator
    public Date(@JsonProperty(value = "in", required = true) String in,
                @JsonProperty(value = "out", required = true) String out,
                @JsonProperty(value = "from", required = true) String from,
                @JsonProperty(value = "to", required = true) String to) {
        super();
        this.in = in;
        this.out = out;
        this.from = from;
        this.to = to;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == fromFormatter) fromFormatter = DateTimeFormatter.ofPattern(from);
        if (null == toFormatter) toFormatter = DateTimeFormatter.ofPattern(to);
        String value = context.getField(in);
        ZonedDateTime original = ZonedDateTime.parse(value, fromFormatter);
        String reformatted = original.format(toFormatter);
        context.addField(out, reformatted);
        return EMPTY;
    }
}