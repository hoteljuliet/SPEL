package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepPredicateComplex;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "and")
public class And extends StepPredicateComplex implements Serializable {

    public And() {
        super();
    }

    /**
     * @param context the context
     * @return true iff all predicates return true
     * @throws Exception exception
     */
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        boolean retval = true;
        for (StepBase s : subPredicate) {
            Optional<Boolean> eval = s.execute(context);
            if (eval.isPresent() && eval.get()) {
                continue;
            }
            else {
                retval = false;
                break;
            }
        }
        return Optional.of(retval);
    }
}
