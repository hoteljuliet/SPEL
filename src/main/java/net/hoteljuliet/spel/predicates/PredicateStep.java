package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Pipeline;
import net.hoteljuliet.spel.Step;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class PredicateStep extends Step implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(PredicateStep.class);

    /**
     * Note that exceptions in Predicates evaluate to false
     * @param t
     * @param context
     * @return
     */
    @Override
    protected Optional<Boolean> handleException(Throwable t, Context context) {
        context.getMetrics(name).exceptionThrown.increment();
        String rootCase = ExceptionUtils.getRootCauseMessage(t);
        context.getMetrics(name).exceptionsCounter.put(rootCase);
        return COMMAND_FALSE;
    }

    public void restore() {
        super.restore();
        logger.debug(this.name + "::restore()");
    }
}
