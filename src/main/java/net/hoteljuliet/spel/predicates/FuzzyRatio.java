package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.StepPredicate;
import net.hoteljuliet.spel.mustache.TemplateLiteral;

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