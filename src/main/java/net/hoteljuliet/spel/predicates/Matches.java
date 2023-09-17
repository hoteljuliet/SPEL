package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepPredicate;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Step(tag = "matches")
public class Matches extends StepPredicate implements Serializable {

    private final String source;
    private final String regex;
    private final Pattern pattern;

    @JsonCreator
    public Matches(@JsonProperty(value = "source", required = true) String source,
                   @JsonProperty(value = "regex", required = true) String regex) {
        super();
        this.source = source;
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Optional<Boolean> retVal;

        if (context.hasField(source)) {
            String value = context.getField(source);
            Matcher matcher = pattern.matcher(value);

            if (matcher.matches()) {
                retVal = TRUE;
            }
            else {
                retVal = FALSE;
            }
        } else {
            missingField();
            retVal = FALSE;
        }

        return retVal;
    }
}