package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepPredicateComplex;
import io.github.hoteljuliet.spel.StepBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

@Step(tag = "or")
public class Or extends StepPredicateComplex implements Serializable {

    public Or() {
        subPredicate = new ArrayList<>();
    }

    /**
     * @param context the context
     * @return true iff one predicates return true
     * @throws Exception exception
     */
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        boolean retval = false;
        for (StepBase s : subPredicate) {

            Optional<Boolean> eval = s.execute(context);
            if (eval.isPresent() && eval.get()) {
                retval = true;
                break;
            }
            else {
                continue;
            }
        }
        return Optional.of(retval);
    }
}