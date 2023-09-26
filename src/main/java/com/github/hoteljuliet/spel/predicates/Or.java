package com.github.hoteljuliet.spel.predicates;

import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepPredicateComplex;
import com.github.hoteljuliet.spel.StepBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

@Step(tag = "or")
public class Or extends StepPredicateComplex implements Serializable {

    public Or() {
        subPredicate = new ArrayList<>();
    }

    /**
     * return true iff one predicates return true
     * @param context
     * @return
     * @throws Exception
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