package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;
import java.util.Optional;

public abstract class ComplexPredicateStep extends PredicateStep {
    public List<Step> subPredicate;

    @Override
    protected Optional<Boolean> handleException(Throwable t, Context context) {
        context.getMetrics(name).exceptionThrown.increment();
        String rootCase = ExceptionUtils.getRootCauseMessage(t);
        context.getMetrics(name).exceptionsCounter.put(rootCase);
        return COMMAND_FALSE;
    }
}
