package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.BaseStep;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Or extends ComplexPredicateBaseStep implements Serializable {

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
        for (BaseStep s : subPredicate) {

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