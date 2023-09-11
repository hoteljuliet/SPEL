package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.Optional;

public class HasValue extends PredicateStep implements Serializable {
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return Optional.empty();
    }
}
