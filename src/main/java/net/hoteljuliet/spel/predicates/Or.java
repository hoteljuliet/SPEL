package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Or extends ComplexPredicateStep implements Serializable {

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
        for (Step s : subPredicate) {

            Optional<Boolean> eval = s.execute(context);
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