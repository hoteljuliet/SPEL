package com.github.hoteljuliet.spel.predicates;

import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepPredicateComplex;
import com.github.hoteljuliet.spel.StepBase;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "not")
public class Not extends StepPredicateComplex implements Serializable {

    public Not() {
        super();
    }

    /**
     * return false iff all predicates return true
     * @param context
     * @return
     * @throws Exception
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
