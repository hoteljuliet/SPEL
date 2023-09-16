package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.BaseStep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ComplexPredicateBaseStep extends PredicateBaseStep implements Serializable {
    public List<BaseStep> subPredicate;

    public ComplexPredicateBaseStep() {
        subPredicate = new ArrayList<>();
    }

    @Override
    public void snapshot() {
        super.snapshot();
        for (BaseStep s : subPredicate) {
            s.snapshot();
        }
    }

    @Override
    public void restore() {
        super.restore();
        for (BaseStep s : subPredicate) {
            s.restore();
        }
    }
}
