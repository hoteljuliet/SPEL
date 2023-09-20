package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.StepPredicate;

import java.util.Optional;

/**
 * Returns true iff the event is a duplicate of another event
 */
@Step(tag = "duplicate")
public class Duplicate extends StepPredicate {

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return Optional.empty();
    }

    @Override
    public void clear() {

    }
}
