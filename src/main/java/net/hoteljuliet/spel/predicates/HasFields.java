package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.StepPredicate;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "has-fields")
public class HasFields extends StepPredicate implements Serializable {
    private List<String> sources;

    @JsonCreator
    public HasFields(@JsonProperty(value = "sources", required = true) List<String> sources) {
        super();
        this.sources = sources;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Optional<Boolean> retVal = TRUE;
        for (String source : sources) {
            if (!context.hasField(source)) {
                retVal = FALSE;
                break;
            }
        }
        return retVal;
    }
}
