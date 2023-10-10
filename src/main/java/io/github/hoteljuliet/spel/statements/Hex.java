package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import io.github.hoteljuliet.spel.Action;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "hex")
public class Hex extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final Action action;
    private transient org.apache.commons.codec.binary.Hex hex;

    /**
     *
     * @param in a path in the Context to a String
     * @param out a path in the Context to where an ASCII Hex String of the hash will be placed
     * @param action must be "ENCODE" or "DECODE" see {@link io.github.hoteljuliet.spel.Action}
     */
    @JsonCreator
    public Hex(@JsonProperty(value = "in", required = true) String in,
               @JsonProperty(value = "out", required = true) String out,
               @JsonProperty(value = "action", required = true) Action action) {
        super();
        this.in = in;
        this.out = out;
        this.action = action;
        this.hex = new org.apache.commons.codec.binary.Hex();
        Preconditions.checkArgument(action.equals(Action.ENCODE) || action.equals(Action.DECODE), "invalid action %s", action);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == hex) hex = new org.apache.commons.codec.binary.Hex();

        String value = context.getField(in);
        if (Action.ENCODE == action) {
            String encodedString = new String(hex.encode(value.getBytes()));
            context.addField(out, encodedString);
        } else {
            String decodedValue = new String(hex.decode(value.getBytes()));
            context.addField(out, decodedValue);
        }
        return EMPTY;
    }
}
