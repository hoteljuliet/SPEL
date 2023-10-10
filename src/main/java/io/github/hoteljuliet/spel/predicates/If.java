package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepPredicate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

@Step(tag = "if")
public class If extends StepPredicate implements Serializable {

    public If() {
        super();
        onTrue = new ArrayList<>();
        onFalse = new ArrayList<>();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        Optional<Boolean> eval = predicate.execute(context);

        if (eval.isPresent() && eval.get()) {
            for (StepBase stepBase : onTrue) {
                stepBase.execute(context);
            }
        }
        else {
            for (StepBase stepBase : onFalse) {
                stepBase.execute(context);
            }
        }
        return eval;
    }
}
