package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import net.hoteljuliet.spel.*;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Step(tag = "b64")
public class B64 extends StepStatement implements Serializable {
    private final String source;
    private final String dest;
    private final Action action;
    private transient Base64 base64;

    @JsonCreator
    public B64(@JsonProperty(value = "source", required = true) String source,
               @JsonProperty(value = "dest", required = true) String dest,
               @JsonProperty(value = "action", required = true) Action action) {
        super();
        this.source = source;
        this.dest = dest;
        this.action = action;
        this.base64 = new Base64();
        Preconditions.checkArgument(action.equals(Action.ENCODE) || action.equals(Action.DECODE), "invalid action %s", action);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (null == base64) base64 = new Base64();

        String value = context.getField(source);
        if (Action.ENCODE == action) {
            String encodedString = base64.encodeAsString(value.getBytes(StandardCharsets.UTF_8));
            context.addField(dest, encodedString);
        } else {
            String decodedValue = new String(base64.decode(value));
            context.addField(dest, decodedValue);
        }
        return StepBase.EMPTY;
    }
}
