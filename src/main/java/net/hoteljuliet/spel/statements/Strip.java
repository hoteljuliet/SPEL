package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;

import java.util.Optional;

public class Strip extends StatementStep {
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return Optional.empty();
    }
}
