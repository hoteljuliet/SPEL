package net.hoteljuliet.spel;

public class PredicateResult extends StatementResult {
    private final Long evalTrue;
    private final Long evalFalse;

    public PredicateResult(StepPredicate stepPredicate) {
        super(stepPredicate);
        evalTrue = stepPredicate.evalTrue.longValue();
        evalFalse = stepPredicate.evalFalse.longValue();
    }
}
