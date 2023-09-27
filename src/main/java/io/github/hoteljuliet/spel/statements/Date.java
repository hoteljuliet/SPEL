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
    private String source;
    private String dest;
    private String from;
    private String to;

    private transient DateTimeFormatter fromFormatter;
    private transient DateTimeFormatter toFormatter;

    @JsonCreator
    public Date(@JsonProperty(value = "source", required = true) String source,
                @JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "from", required = true) String from,
                @JsonProperty(value = "to", required = true) String to) {
        super();
        this.source = source;
        this.dest = dest;
        this.from = from;
        this.to = to;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == fromFormatter) fromFormatter = DateTimeFormatter.ofPattern(from);
        if (null == toFormatter) toFormatter = DateTimeFormatter.ofPattern(to);
        String value = context.getField(source);
        ZonedDateTime original = ZonedDateTime.parse(value, fromFormatter);
        String reformatted = original.format(toFormatter);
        context.addField(dest, reformatted);
        return EMPTY;
    }
}