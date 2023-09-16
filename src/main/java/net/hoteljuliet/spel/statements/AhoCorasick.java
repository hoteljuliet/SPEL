package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.util.Optional;

@Step(tag = "aho-corasick")
public class AhoCorasick extends StatementBaseStep {
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return Optional.empty();
    }
}
