package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.util.Optional;

@Step(tag = "grok")
public class Grok extends StatementBaseStep {
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return Optional.empty();
    }
}
