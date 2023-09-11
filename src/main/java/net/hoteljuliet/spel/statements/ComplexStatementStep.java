package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.List;

public abstract class ComplexStatementStep extends StatementStep implements Serializable {
    public List<Step> subStatements;

    @Override
    public void restore() {
        super.restore();
        for (Step s : subStatements) {
            s.restore();
        }
    }
}
