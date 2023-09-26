package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import net.hoteljuliet.spel.*;

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
    private final String value;
    private final String charset;
    private final String dest;
    private final Action action;

    @JsonCreator
    public Url(@JsonProperty(value = "value", required = true) String value,
               @JsonProperty(value = "charset", required = true) String charset,
               @JsonProperty(value = "dest", required = true) String dest,
               @JsonProperty(value = "action", required = true) Action action) {
        super();
        this.value = value;
        this.charset = charset;
        this.dest = dest;
        this.action = action;
        Preconditions.checkArgument(action.equals(Action.ENCODE) || action.equals(Action.DECODE), "invalid action %s", action);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String target = context.getField(value);
        if (Action.ENCODE == action) {
            String encodedString = URLEncoder.encode(target, Charset.forName(charset));
            context.addField(dest, encodedString);
        } else {
            String decodedValue = URLDecoder.decode(target, charset);
            context.addField(dest, decodedValue);
        }
        return EMPTY;
    }
}
