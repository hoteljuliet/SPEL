package net.hoteljuliet.spel.statements;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.StepStatementComplex;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Step(tag = "sort")
public class Sort extends StepStatementComplex implements Serializable, Comparator<Map<String, Object>> {

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
     * Compares its two arguments for order. Returns a negative integer, zero, or a positive
     * integer as the first argument is less than, equal to, or greater than the second.
     * @param map1
     * @param map2
     * @return
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
        return sortContext.getField("_compare");
    }
}
