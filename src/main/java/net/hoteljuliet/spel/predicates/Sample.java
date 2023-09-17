package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.*;
import net.hoteljuliet.spel.StepPredicate;
import net.hoteljuliet.spel.StepBase;

import java.io.Serializable;
import java.util.Optional;
import java.util.Random;

@Step(tag = "sample")
public class Sample extends StepPredicate implements Serializable {
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
        return (random.nextInt(100) < percentage) ? StepBase.TRUE : StepBase.FALSE;
    }
}
