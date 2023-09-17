package net.hoteljuliet.spel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class StepPredicateComplex extends StepPredicate implements Serializable {
    public List<StepBase> subPredicate;

    public StepPredicateComplex() {
        subPredicate = new ArrayList<>();
    }

}
