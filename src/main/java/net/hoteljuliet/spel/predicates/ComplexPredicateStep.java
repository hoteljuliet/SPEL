package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ComplexPredicateStep extends PredicateStep implements Serializable {
    public List<Step> subPredicate;

    public ComplexPredicateStep() {
        subPredicate = new ArrayList<>();
    }

    @Override
    public void snapshot() {
        super.snapshot();
        for (Step s : subPredicate) {
            s.snapshot();
        }
    }

    @Override
    public void restore() {
        super.restore();
        for (Step s : subPredicate) {
            s.restore();
        }
    }
}
