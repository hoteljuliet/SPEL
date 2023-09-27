package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.util.Optional;

@Step(tag = "aho-corasick")
public class AhoCorasick extends StepStatement {
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return Optional.empty();
    }
}
