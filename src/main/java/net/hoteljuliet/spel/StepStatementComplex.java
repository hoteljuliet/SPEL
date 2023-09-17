package net.hoteljuliet.spel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class StepStatementComplex extends StepStatement implements Serializable {
    public List<StepBase> subStatements;

    public StepStatementComplex() {
        subStatements = new ArrayList<>();
    }

}
