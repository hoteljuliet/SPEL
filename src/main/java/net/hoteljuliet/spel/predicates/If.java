package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.BaseStep;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class If extends PredicateBaseStep implements Serializable {

    public List<BaseStep> onTrue;
    public List<BaseStep> onFalse;

    public BaseStep predicate;

    public If() {
        super();
        onTrue = new ArrayList<>();
        onFalse = new ArrayList<>();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        Optional<Boolean> eval = predicate.execute(context);

        if (eval.isPresent() && eval.get()) {
            for (BaseStep baseStep : onTrue) {
                baseStep.execute(context);
            }
        }
        else {
            for (BaseStep baseStep : onFalse) {
                baseStep.execute(context);
            }
        }
        return BaseStep.NEITHER;
    }

    @Override
    public void snapshot() {
        super.snapshot();
        predicate.snapshot();
        for (BaseStep s : onTrue) {
            s.snapshot();
        }
        for (BaseStep s : onFalse) {
            s.snapshot();
        }
    }

    @Override
    public void restore() {
        super.restore();
        predicate.restore();
        for (BaseStep s : onTrue) {
            s.restore();
        }
        for (BaseStep s : onFalse) {
            s.restore();
        }
    }
}
