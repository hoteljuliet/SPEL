package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepPredicate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class If extends StepPredicate implements Serializable {

    public List<StepBase> onTrue;
    public List<StepBase> onFalse;

    public StepBase predicate;

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
        return StepBase.NEITHER;
    }
}
