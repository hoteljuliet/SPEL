package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepPredicateComplex;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "not")
public class Not extends StepPredicateComplex implements Serializable {

    public Not() {
        super();
    }

    /**
     * @param context the context
     * @return false iff all predicates return true
     * @throws Exception exception
     */
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        boolean retVal = true;
        for (StepBase s : subPredicate) {
            Optional<Boolean> eval = s.execute(context);
            if (eval.isPresent() && eval.get()) {
                continue;
            }
            else {
                retVal = false;
                break;
            }
        }

        boolean negated = !retVal;
        return Optional.of(negated);
    }
}
