package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;

import java.util.ArrayList;
import java.util.Optional;

public class And extends ComplexPredicateStep {

    public And() {
        subPredicate = new ArrayList<>();
    }

    /**
     * return true iff all predicates return true
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        boolean retval = true;
        for (Command c : subPredicate) {
            Optional<Boolean> eval = c.execute(context);
            if (eval.isPresent() && eval.get()) {
                continue;
            }
            else {
                retval = false;
                break;
            }
        }
        return Optional.of(retval);
    }
}
