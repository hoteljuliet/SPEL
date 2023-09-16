package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.BaseStep;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForEach extends ComplexStatementBaseStep {

    private String source;

    public ForEach(String source) {
        super();
        this.source = source;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        List<Object> list = context.getField(source);
        List<Object> processedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Context forEachContext = new Context();
            forEachContext.put("i", list.get(i));
            for (BaseStep s : subStatements) {
                s.execute(forEachContext);
            }
            Object result = forEachContext.get("i");
            processedList.add(result);
        }
        context.replaceFieldValue(source, processedList);
        return NEITHER;
    }
}
