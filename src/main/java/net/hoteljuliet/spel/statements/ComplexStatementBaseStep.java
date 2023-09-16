package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.BaseStep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ComplexStatementBaseStep extends StatementBaseStep implements Serializable {
    public List<BaseStep> subStatements;

    public ComplexStatementBaseStep() {
        subStatements = new ArrayList<>();
    }

    @Override
    public void snapshot() {
        super.snapshot();
        for (BaseStep s : subStatements) {
            s.snapshot();
        }
    }

    @Override
    public void restore() {
        super.restore();
        for (BaseStep s : subStatements) {
            s.restore();
        }
    }
}
