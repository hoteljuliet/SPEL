package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "add-i")
public class AddI extends StatementBaseStep implements Serializable {

    private final String dest;
    private final Object value;

    @JsonCreator
    public AddI(@JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "value", required = true) Object value) {
        super();
        this.dest = dest;
        this.value = value;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        context.addField(dest, value);
        return NEITHER;
    }
}