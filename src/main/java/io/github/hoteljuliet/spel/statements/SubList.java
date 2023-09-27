package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.StepStatementComplex;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Step(tag = "sublist")
public class SubList extends StepStatement implements Serializable {

    private final String list;
    private final Integer from;
    private final Integer to;

    @JsonCreator
    public SubList(@JsonProperty(value = "list", required = true) String list,
                   @JsonProperty(value = "list", required = true) Integer from,
                   @JsonProperty(value = "list", required = true) Integer to) {
        super();
        this.list = list;
        this.from = from;
        this.to = to;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        List<Map<String, Object>> originalList = context.getField(list);
        List<Map<String, Object>> subList = originalList.subList(from, to);
        context.replaceFieldValue(list, subList);
        return EMPTY;
    }
}