package com.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepPredicate;
import com.github.hoteljuliet.spel.mustache.TemplateLiteral;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "fuzzy-ratio")
public class FuzzyRatio extends StepPredicate implements Serializable {

    private final TemplateLiteral first;
    private final TemplateLiteral second;
    private final Integer ratio;

    @JsonCreator
    public FuzzyRatio(@JsonProperty(value = "first", required = true) TemplateLiteral first,
                      @JsonProperty(value = "second", required = true) TemplateLiteral second,
                      @JsonProperty(value = "ratio", required = true) Integer ratio) {
        super();
        this.first = first;
        this.second = second;
        this.ratio = ratio;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object firstValue = first.get(context);
        Object secondValue = second.get(context);
        int result = FuzzySearch.ratio(firstValue.toString(), secondValue.toString());
        return (result >= ratio) ? TRUE : FALSE;
    }
}