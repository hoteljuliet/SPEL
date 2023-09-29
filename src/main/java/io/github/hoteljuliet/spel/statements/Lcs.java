package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.apache.commons.text.similarity.LongestCommonSubsequence;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "lcs")
public class Lcs extends StepStatement implements Serializable {
    private final TemplateLiteral first;
    private final TemplateLiteral second;
    private final String out;
    private transient LongestCommonSubsequence longestCommonSubsequence;

    /**
     *
     * @param first the first string. See {@link io.github.hoteljuliet.spel.mustache.TemplateLiteral}
     * @param second the second string. See {@link io.github.hoteljuliet.spel.mustache.TemplateLiteral}
     * @param out a path in the Context to where the LCS String will be placed
     */
    @JsonCreator
    public Lcs(@JsonProperty(value = "first", required = true) TemplateLiteral first,
               @JsonProperty(value = "second", required = true) TemplateLiteral second,
               @JsonProperty(value = "out", required = true) String out) {
        super();
        this.first = first;
        this.second = second;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == longestCommonSubsequence) longestCommonSubsequence = new LongestCommonSubsequence();
        String firstValue = first.get(context);
        String secondValue = second.get(context);
        CharSequence charSequence = longestCommonSubsequence.longestCommonSubsequence(firstValue, secondValue);
        context.addField(out, charSequence.toString());
        return StepBase.EMPTY;
    }
}
