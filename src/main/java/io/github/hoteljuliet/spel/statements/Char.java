package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Convert a string from one character encoding to another
 */
@Step(tag = "char")
public class Char extends StepStatement implements Serializable {
    private final String source;
    private final String dest;
    private final Charset from;
    private final Charset to;
    @JsonCreator
    public Char(@JsonProperty(value = "source", required = true) String source,
                @JsonProperty(value = "from", required = true) String from,
                @JsonProperty(value = "to", required = true) String to,
                @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.from = Charset.forName(from);
        this.to = Charset.forName(to);
        this.dest = dest;
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(source);
        byte[] bytes = value.getBytes(from);
        String converted = new String(bytes, to);
        context.addField(dest, converted);
        return StepBase.EMPTY;
    }
}
