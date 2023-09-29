package io.github.hoteljuliet.spel.statements;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepStatementComplex;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Given a list of values and a list of Steps, use the steps in a comparator and sort the list of values.
 */
@Step(tag = "sort")
public class Sort extends StepStatementComplex implements Serializable, Comparator<Map<String, Object>> {

    /**
     * The field where the output of the comparison is expected to be stored.
     */
    public static final String COMPARE_OUTPUT = "_compare";
    private final String list;

    public Sort(String source) {
        super();
        this.list = source;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        List<Map<String, Object>> l = context.getField(list);
        l.sort(this);
        return EMPTY;
    }

    /**
     * Compares its two arguments for order.
     * @param map1 the first ma
     * @param map2 the second map
     * @return Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    @Override
    public int compare(Map<String, Object> map1, Map<String, Object> map2) {
        Context sortContext = new Context();
        sortContext.addField("map1", map1);
        sortContext.addField("map2", map2);

        for (StepBase stepBase : subStatements) {
            try {
                stepBase.execute(sortContext);
            }
            catch(Exception ignored) {}
        }
        return sortContext.getField(COMPARE_OUTPUT);
    }
}
