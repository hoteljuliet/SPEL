package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.util.Optional;

/**
 * Gives the paths to 2 dates, output the duration in the unit provided to the destination provided
 */
@Step(tag = "duration")
public class Duration extends StepStatement {
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        // TODO: implement
        return Optional.empty();
    }
}
