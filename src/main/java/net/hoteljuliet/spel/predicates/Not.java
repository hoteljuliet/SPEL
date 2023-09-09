package net.hoteljuliet.spel.predicates;

import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Not extends Step {

    public List<Step> predicate;

    public Not() {
        predicate = new ArrayList<>();
    }

    /**
     * return false iff all predicates return true
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        boolean retval = true;
        for (Command c : predicate) {
            Optional<Boolean> eval = c.execute(context);
            if (eval.isPresent() && eval.get()) {
                continue;
            }
            else {
                retval = false;
                break;
            }
        }
        return Optional.of(!retval);
    }
}