package com.github.hoteljuliet.spel;

public class PredicateResult extends StatementResult {
    public final Long evalTrue;
    public final Long evalFalse;

    public PredicateResult(StepPredicate stepPredicate) {
        super(stepPredicate);
        evalTrue = stepPredicate.evalTrue.longValue();
        evalFalse = stepPredicate.evalFalse.longValue();
    }
}
