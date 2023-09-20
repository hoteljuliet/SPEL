package net.hoteljuliet.spel;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public abstract class StepPredicate extends StepBase implements Serializable {

    private final LongAdder evalTrue;
    private final LongAdder evalFalse;

    public StepPredicate() {
        evalTrue = new LongAdder();
        evalFalse = new LongAdder();
    }

    /**
     * Increment the true/false counters for this predicate
     * @param evaluation
     * @param context
     */
    @Override
    public void after(Optional<Boolean> evaluation, Context context) {
        super.after(evaluation, context);
        if (evaluation.equals(TRUE)) {
            evalTrue();
        }
        else if (evaluation.equals(FALSE)) {
            evalFalse();
        }
    }

    public void evalTrue() {
        evalTrue.increment();
    }

    public void evalFalse() {
        evalFalse.increment();
    }

}
