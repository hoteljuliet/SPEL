package com.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepPredicate;
import com.github.hoteljuliet.spel.*;

import java.io.Serializable;
import java.util.Optional;
import java.util.Random;

@Step(tag = "random-sample")
public class RandomSample extends StepPredicate implements Serializable {
    private final Integer percentage;
    private final Random random;

    @JsonCreator
    public RandomSample(@JsonProperty(value = "pct", required = true) Integer percentage) {
        super();
        this.percentage = percentage;
        this.random = new Random();
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return (random.nextInt(100) < percentage) ? TRUE : FALSE;
    }
}
