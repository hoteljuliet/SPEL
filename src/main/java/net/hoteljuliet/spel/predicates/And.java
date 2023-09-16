package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.BaseStep;

import java.io.Serializable;
import java.util.Optional;

public class And extends ComplexPredicateBaseStep implements Serializable {

    public And() {
        super();
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
        for (BaseStep s : subPredicate) {
            Optional<Boolean> eval = s.execute(context);
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
