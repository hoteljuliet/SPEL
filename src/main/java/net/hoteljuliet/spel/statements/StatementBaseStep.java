package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.BaseStep;
import net.hoteljuliet.spel.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;

public abstract class StatementBaseStep extends BaseStep implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(StatementBaseStep.class);

    public StatementBaseStep() {
        super();
    }

    @Override
    protected Optional<Boolean> onException(Throwable t, Context context) {
        return NEITHER;
    }

    public void restore() {
        super.restore();
        logger.debug(this.name + "::restore()");
    }
}
