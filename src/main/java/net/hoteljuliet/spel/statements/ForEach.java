package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.StepStatementComplex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForEach extends StepStatementComplex implements Serializable {

    private final String list;

    public ForEach(String source) {
        super();
        this.list = source;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        List<Object> l = context.getField(list);
        List<Object> processedList = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            Context forEachContext = new Context(context.pipeline);
            forEachContext.put("i", l.get(i));
            for (StepBase s : subStatements) {
                s.execute(forEachContext);
            }
            Object result = forEachContext.get("i");
            processedList.add(result);
        }
        context.replaceFieldValue(list, processedList);
        return EMPTY;
    }
}
