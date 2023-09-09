package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Step;

import java.util.concurrent.atomic.LongAdder;

public abstract class PredicateStep extends Step {
    public LongAdder evalTrue = new LongAdder();
    public LongAdder evalFalse = new LongAdder();
}
