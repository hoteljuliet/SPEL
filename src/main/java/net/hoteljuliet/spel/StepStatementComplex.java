package net.hoteljuliet.spel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class StepStatementComplex extends StepStatement implements Serializable {
    public List<StepBase> subStatements;

    public StepStatementComplex() {
        subStatements = new ArrayList<>();
    }

    @Override
    public void toMermaid(Optional<StepBase> parent, Optional<Boolean> predicatePath, StringBuilder stringBuilder) {
        super.toMermaid(parent, predicatePath, stringBuilder);

        StepBase pointer = this;
        for (int i = 0; i < subStatements.size(); i++) {
            subStatements.get(i).toMermaid(Optional.of(pointer), Optional.empty(), stringBuilder);
            pointer = subStatements.get(i);
        }
    }
}
