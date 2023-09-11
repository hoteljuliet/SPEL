package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class Not extends ComplexPredicateStep implements Serializable {

    public Not() {
        subPredicate = new ArrayList<>();
    }

    /**
     * return false iff all predicates return true
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        boolean retVal = true;
        for (Command c : subPredicate) {
            Optional<Boolean> eval = c.execute(context);
            if (eval.isPresent() && eval.get()) {
                continue;
            }
            else {
                retVal = false;
                break;
            }
        }

        boolean negated = !retVal;
        return Optional.of(negated);
    }
}
