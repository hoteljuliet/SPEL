package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "sublist")
public class SubList extends StepStatement implements Serializable {

    private final String in;
    private final Integer from;
    private final Integer to;

    @JsonCreator
    public SubList(@JsonProperty(value = "in", required = true) String in,
                   @JsonProperty(value = "from", required = true) Integer from,
                   @JsonProperty(value = "to", required = true) Integer to) {
        super();
        this.in = in;
        this.from = from;
        this.to = to;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        List<Object> originalList = context.getField(in);
        List<Object> subList = originalList.subList(from, to);
        context.replaceFieldValue(in, subList);
        return EMPTY;
    }
}