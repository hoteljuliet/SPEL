package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Date extends StatementStep implements Serializable {
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
        fromFormatter = DateTimeFormatter.ofPattern(from);
        toFormatter = DateTimeFormatter.ofPattern(to);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (context.hasField(source)) {
            String value = context.getField(source);
            ZonedDateTime original = ZonedDateTime.parse(value, fromFormatter);
            String reformatted = original.format(toFormatter);
            context.addField(dest, reformatted);
        }
        else {
            missingField.increment();
        }
        return COMMAND_NEITHER;
    }

    @Override
    public void restore() {
        super.restore();
        fromFormatter = DateTimeFormatter.ofPattern(from);
        toFormatter = DateTimeFormatter.ofPattern(to);
    }
}