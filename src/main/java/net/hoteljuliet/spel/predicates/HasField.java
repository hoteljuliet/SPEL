package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.BaseStep;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "has-field")
public class HasField extends PredicateBaseStep implements Serializable {
    private String source;

    @JsonCreator
    public HasField(@JsonProperty(value = "source", required = true) String source) {
        super();
        this.source = source;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            return BaseStep.TRUE;
        }
        else {
            return BaseStep.FALSE;
        }
    }
}
