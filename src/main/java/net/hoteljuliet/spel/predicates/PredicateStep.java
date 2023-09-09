package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public abstract class PredicateStep extends Step {

    @Override
    protected Optional<Boolean> handleException(Throwable t, Context context) {
        context.getMetrics(name).exceptionThrown.increment();
        String rootCase = ExceptionUtils.getRootCauseMessage(t);
        context.getMetrics(name).exceptionsCounter.put(rootCase);
        return COMMAND_FALSE;
    }
}
