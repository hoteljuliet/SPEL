package net.hoteljuliet.spel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public abstract class StepPredicate extends StepBase implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(StepPredicate.class);
    protected LongAdder evalTrue;
    protected LongAdder evalFalse;

    public StepPredicate() {
        super();
        evalTrue = new LongAdder();
        evalFalse = new LongAdder();
    }

    /**
     * Note that exceptions in Predicates evaluate to false
     * @param t
     * @param context
     * @return
     */
    @Override
    protected Optional<Boolean> onException(Throwable t, Context context) {
        softFailure.increment();
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
            evalTrue.increment();
        }
        else if (evaluation.equals(FALSE)) {
            evalFalse.increment();
        }
    }
}
