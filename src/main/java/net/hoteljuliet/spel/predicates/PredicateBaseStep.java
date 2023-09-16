package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.BaseStep;
import net.hoteljuliet.spel.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public abstract class PredicateBaseStep extends BaseStep implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(PredicateBaseStep.class);
    protected LongAdder evalTrue;
    protected LongAdder evalFalse;

    public PredicateBaseStep() {
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

    @Override
    public void restore() {
        super.restore();
        logger.debug(this.name + "::restore()");
    }
}
