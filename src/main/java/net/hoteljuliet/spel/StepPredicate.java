package net.hoteljuliet.spel;

import java.io.Serializable;
import java.util.Optional;

public abstract class StepPredicate extends StepBase implements Serializable {

    /**
     * Note that exceptions in Predicates evaluate to false
     * @param t
     * @param context
     * @return
     */
    @Override
    protected Optional<Boolean> onException(Throwable t, Context context) {
        softFailure();
        return FALSE;
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
}
