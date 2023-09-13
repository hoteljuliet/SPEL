package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;

public abstract class StatementStep extends Step implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(StatementStep.class);

    public StatementStep() {
        super();
    }

    @Override
    protected Optional<Boolean> onException(Throwable t, Context context) {
        return COMMAND_NEITHER;
    }

    public void restore() {
        super.restore();
        logger.debug(this.name + "::restore()");
    }
}
