package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Optional;

public abstract class StatementStep extends Step {
    @Override
    protected Optional<Boolean> handleException(Throwable t, Context context) {
        context.getMetrics(name).exceptionThrown.increment();
        String rootCase = ExceptionUtils.getRootCauseMessage(t);
        context.getMetrics(name).exceptionsCounter.put(rootCase);
        return COMMAND_NEITHER;
    }
}
