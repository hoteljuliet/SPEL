package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class If extends PredicateStep implements Serializable {

    public List<Step> onTrue;
    public List<Step> onFalse;

    public Step predicate;

    public If() {
        super();
        onTrue = new ArrayList<>();
        onFalse = new ArrayList<>();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        Optional<Boolean> eval = predicate.execute(context);

        if (eval.isPresent() && eval.get()) {
            for (Step step : onTrue) {
                step.execute(context);
            }
        }
        else {
            for (Step step : onFalse) {
                step.execute(context);
            }
        }
        return Step.COMMAND_NEITHER;
    }

    @Override
    public void snapshot() {
        super.snapshot();
        predicate.snapshot();
        for (Step s : onTrue) {
            s.snapshot();
        }
        for (Step s : onFalse) {
            s.snapshot();
        }
    }

    @Override
    public void restore() {
        super.restore();
        predicate.restore();
        for (Step s : onTrue) {
            s.restore();
        }
        for (Step s : onFalse) {
            s.restore();
        }
    }
}
