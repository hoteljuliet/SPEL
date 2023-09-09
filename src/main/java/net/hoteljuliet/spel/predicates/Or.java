package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;

import java.util.ArrayList;
import java.util.Optional;

public class Or extends ComplexPredicateStep {

    public Or() {
        subPredicate = new ArrayList<>();
    }

    /**
     * return true iff one predicates return true
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        boolean retval = false;
        for (Command c : subPredicate) {

            Optional<Boolean> eval = c.execute(context);
            if (eval.isPresent() && eval.get()) {
                retval = true;
                break;
            }
            else {
                continue;
            }
        }
        return Optional.of(retval);
    }
}