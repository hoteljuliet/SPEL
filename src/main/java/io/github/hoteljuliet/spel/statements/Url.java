package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import io.github.hoteljuliet.spel.Action;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * URL decode or encode a string
 */
@Step(tag = "url")
public class Url extends StepStatement implements Serializable {
    private final String in;
    private final Charset charset;
    private final String out;
    private final Action action;

    @JsonCreator
    public Url(@JsonProperty(value = "in", required = true) String in,
               @JsonProperty(value = "charset", required = true) String charset,
               @JsonProperty(value = "out", required = true) String out,
               @JsonProperty(value = "action", required = true) Action action) {
        super();
        this.in = in;
        this.charset = Charset.forName(charset);
        this.out = out;
        this.action = action;
        Preconditions.checkArgument(action.equals(Action.ENCODE) || action.equals(Action.DECODE), "invalid action %s", action);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(in);
        if (Action.ENCODE == action) {
            String encodedString = URLEncoder.encode(value, charset);
            context.addField(out, encodedString);
        } else {
            String decodedValue = URLDecoder.decode(value, charset);
            context.addField(out, decodedValue);
        }
        return EMPTY;
    }
}
