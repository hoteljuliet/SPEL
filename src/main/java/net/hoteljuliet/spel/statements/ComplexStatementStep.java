package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ComplexStatementStep extends StatementStep implements Serializable {
    public List<Step> subStatements;

    public ComplexStatementStep() {
        subStatements = new ArrayList<>();
    }

    @Override
    public void snapshot() {
        super.snapshot();
        for (Step s : subStatements) {
            s.snapshot();
        }
    }

    @Override
    public void restore() {
        super.restore();
        for (Step s : subStatements) {
            s.restore();
        }
    }
}
