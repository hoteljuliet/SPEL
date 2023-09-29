package io.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepPredicate;

import java.io.Serializable;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Step(tag = "matches")
public class Matches extends StepPredicate implements Serializable {

    private final String in;
    private final String regex;
    private final Pattern pattern;

    @JsonCreator
    public Matches(@JsonProperty(value = "in", required = true) String in,
                   @JsonProperty(value = "regex", required = true) String regex) {
        super();
        this.in = in;
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String v = context.getField(in);
        Matcher matcher = pattern.matcher(v);
        return matcher.matches() ? TRUE : FALSE;
    }
}