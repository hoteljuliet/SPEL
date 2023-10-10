package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import io.github.hoteljuliet.spel.Action;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Step(tag = "b64")
public class B64 extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final Action action;
    private transient Base64 base64;

    /**
     * B64 encode/decode a string
     * @param in the path to a string value in the context
     * @param out  the path into the context where the resulting value will be placed
     * @param action must be "ENCODE" or "DECODE" see {@link io.github.hoteljuliet.spel.Action}
     */
    @JsonCreator
    public B64(@JsonProperty(value = "in", required = true) String in,
               @JsonProperty(value = "out", required = true) String out,
               @JsonProperty(value = "action", required = true) Action action) {
        super();
        this.in = in;
        this.out = out;
        this.action = action;
        this.base64 = new Base64();
        Preconditions.checkArgument(action.equals(Action.ENCODE) || action.equals(Action.DECODE), "invalid action %s", action);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == base64) base64 = new Base64();

        String target = context.getField(in);
        String result;
        if (Action.ENCODE == action) {
            result = base64.encodeAsString(target.getBytes(StandardCharsets.UTF_8));
        } else {
            result = new String(base64.decode(target));
        }
        context.addField(out, result);
        return EMPTY;
    }
}
