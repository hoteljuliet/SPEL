package com.github.hoteljuliet.spel.predicates;

import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepPredicateComplex;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "xor")
public class Xor extends StepPredicateComplex implements Serializable {

    public Xor() {
        super();
    }

    /**
     * return true iff first 2 predicates eval to different values
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Boolean eval1 = subPredicate.get(0).doExecute(context).get();
        Boolean eval2 = subPredicate.get(1).doExecute(context).get();
        return eval1 ^ eval2 ? TRUE : FALSE;
    }
}
