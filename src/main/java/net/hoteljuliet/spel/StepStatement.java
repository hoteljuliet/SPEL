package net.hoteljuliet.spel;

import java.io.Serializable;
import java.util.Optional;

public abstract class StepStatement extends StepBase implements Serializable {

    public StepStatement() {
        super();
    }

    @Override
    protected Optional<Boolean> onException(Throwable t, Context context) {
        return NEITHER;
    }

}
