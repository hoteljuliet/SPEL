package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.BaseStep;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;
import java.util.Random;

@Step(tag = "sample")
public class Sample extends PredicateBaseStep implements Serializable {
    private final Integer percentage;
    private final Random random;

    @JsonCreator
    public Sample(@JsonProperty(value = "percentage", required = true) Integer percentage) {
        super();
        this.percentage = percentage;
        random = new Random();
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return (random.nextInt(100) < percentage) ? BaseStep.TRUE : BaseStep.FALSE;
    }
}
