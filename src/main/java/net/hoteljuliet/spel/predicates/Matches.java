package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matches extends Step {

    private String source;
    private String regex;
    private Pattern pattern;

    @JsonCreator
    public Matches(@JsonProperty(value = "source", required = true) String source,
                   @JsonProperty(value = "regex", required = true) String regex) {
        this.source = source;
        this.regex = regex;
        pattern = Pattern.compile(regex);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Optional<Boolean> retVal;

        try {
            if (context.hasField(source)) {
                String value = context.getField(source);
                Matcher matcher = pattern.matcher(value);
                success.increment();

                if (matcher.matches()) {
                    evalTrue.increment();
                    retVal = COMMAND_TRUE;
                }
                else {
                    evalFalse.increment();
                    retVal = COMMAND_FALSE;
                }
            } else {
                missing.increment();
                retVal = COMMAND_FALSE;
            }
        } catch (Exception ex) {
            handleException(ex);
            retVal = COMMAND_FALSE;
        }
        return retVal;
    }
}