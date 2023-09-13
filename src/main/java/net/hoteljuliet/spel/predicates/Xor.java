package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.Optional;

public class Xor extends ComplexPredicateStep implements Serializable {

    public Xor() {
        super();
    }

    /**
     * return true iff first 2 predicates eval to different values
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Boolean eval1 = subPredicate.get(0).doExecute(context).get();
        Boolean eval2 = subPredicate.get(1).doExecute(context).get();
        return eval1 ^ eval2 ? COMMAND_TRUE : COMMAND_FALSE;
    }
}
