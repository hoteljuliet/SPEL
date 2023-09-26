package com.github.hoteljuliet.spel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class StepPredicateComplex extends StepPredicate implements Serializable {
    public List<StepBase> subPredicate;

    public StepPredicateComplex() {
        subPredicate = new ArrayList<>();
    }

    @Override
    public void toMermaid(Optional<StepBase> parent, Optional<Boolean> predicatePath, StringBuilder stringBuilder) {
        super.toMermaid(parent, predicatePath, stringBuilder);

        StepBase pointer = this;
        for (int i = 0; i < subPredicate.size(); i++) {
            subPredicate.get(i).toMermaid(Optional.of(pointer), Optional.empty(), stringBuilder);
            pointer = subPredicate.get(i);
        }
    }
}
