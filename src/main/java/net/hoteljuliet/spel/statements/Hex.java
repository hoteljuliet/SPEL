package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import net.hoteljuliet.spel.Action;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "hex")
public class Hex extends StepStatement implements Serializable {
    private final String value;
    private final String dest;
    private final Action action;
    private transient org.apache.commons.codec.binary.Hex hex;

    @JsonCreator
    public Hex(@JsonProperty(value = "value", required = true) String value,
               @JsonProperty(value = "dest", required = true) String dest,
               @JsonProperty(value = "action", required = true) Action action) {
        super();
        this.value = value;
        this.dest = dest;
        this.action = action;
        this.hex = new org.apache.commons.codec.binary.Hex();
        Preconditions.checkArgument(action.equals(Action.ENCODE) || action.equals(Action.DECODE), "invalid action %s", action);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == hex) hex = new org.apache.commons.codec.binary.Hex();

        String target = context.getField(value);
        if (Action.ENCODE == action) {
            String encodedString = new String(hex.encode(target.getBytes()));
            context.addField(dest, encodedString);
        } else {
            String decodedValue = new String(hex.decode(target.getBytes()));
            context.addField(dest, decodedValue);
        }
        return EMPTY;
    }
}
