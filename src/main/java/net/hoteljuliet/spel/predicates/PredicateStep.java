package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public abstract class PredicateStep extends Step implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(PredicateStep.class);
    protected LongAdder evalTrue;
    protected LongAdder evalFalse;

    public PredicateStep() {
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
        return COMMAND_FALSE;
    }

    /**
     * Increment the true/false counters for this predicate
     * @param evaluation
     * @param context
     */
    @Override
    public void after(Optional<Boolean> evaluation, Context context) {
        super.after(evaluation, context);
        if (evaluation.equals(COMMAND_TRUE)) {
            evalTrue.increment();
        }
        else if (evaluation.equals(COMMAND_FALSE)) {
            evalFalse.increment();
        }
    }

    @Override
    public void restore() {
        super.restore();
        logger.debug(this.name + "::restore()");
    }
}
