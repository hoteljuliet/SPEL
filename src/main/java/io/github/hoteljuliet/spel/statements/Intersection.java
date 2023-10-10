package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Step(tag = "intersection")
public class Intersection extends StepStatement implements Serializable {
    private final TemplateLiteral first;
    private final TemplateLiteral second;
    private final String out;

    /**
     *
     * @param first the first collection, See {@link io.github.hoteljuliet.spel.mustache.TemplateLiteral}
     * @param second the second collection, See {@link io.github.hoteljuliet.spel.mustache.TemplateLiteral}
     * @param out the path in the context where a List of the computed intersection will be placed
     */
    @JsonCreator
    public Intersection(@JsonProperty(value = "first", required = true) TemplateLiteral first,
                        @JsonProperty(value = "second", required = true) TemplateLiteral second,
                        @JsonProperty(value = "out", required = true) String out) {
        super();
        this.first = first;
        this.second = second;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Collection<Object> firstCollection = first.get(context);
        Collection<Object> secondCollection = second.get(context);

        List<Object> result = firstCollection.stream()
                .distinct()
                .filter(secondCollection::contains)
                .collect(Collectors.toList());

        context.addField(out, result);
        return StepBase.EMPTY;
    }
}