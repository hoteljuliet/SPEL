package com.github.hoteljuliet.spel.statements;

import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepStatement;

import java.util.Optional;

@Step(tag = "aho-corasick")
public class AhoCorasick extends StepStatement {
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return Optional.empty();
    }
}
