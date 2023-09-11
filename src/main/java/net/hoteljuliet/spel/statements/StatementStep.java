package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Pipeline;
import net.hoteljuliet.spel.Step;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;

public abstract class StatementStep extends Step implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(StatementStep.class);

    @Override
    protected Optional<Boolean> handleException(Throwable t, Context context) {
        context.getMetrics(name).exceptionThrown.increment();
        String rootCase = ExceptionUtils.getRootCauseMessage(t);
        context.getMetrics(name).exceptionsCounter.put(rootCase);
        return COMMAND_NEITHER;
    }

    public void restore() {
        super.restore();
        logger.debug(this.name + "::restore()");
    }
}
