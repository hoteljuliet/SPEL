package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.StepPredicateComplex;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.Optional;

public class Not extends StepPredicateComplex implements Serializable {

    public Not() {
        super();
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
        for (StepBase s : subPredicate) {
            Optional<Boolean> eval = s.execute(context);
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
