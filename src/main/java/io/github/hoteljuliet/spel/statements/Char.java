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
    private final String in;
    private final String out;
    private final Charset from;
    private final Charset to;

    /**
     * Convert a string from one character encoding to another
     * @param in a path to a String in the context
     * @param from the original charset, must be one of {@link java.nio.charset.Charset}
     * @param to the target charset, must be one of {@link java.nio.charset.Charset}
     * @param out the path in the context where the reformatted string will be placed
     */
    @JsonCreator
    public Char(@JsonProperty(value = "in", required = true) String in,
                @JsonProperty(value = "from", required = true) String from,
                @JsonProperty(value = "to", required = true) String to,
                @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.from = Charset.forName(from);
        this.to = Charset.forName(to);
        this.out = out;
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(in);
        byte[] bytes = value.getBytes(from);
        String converted = new String(bytes, to);
        context.addField(out, converted);
        return StepBase.EMPTY;
    }
}
