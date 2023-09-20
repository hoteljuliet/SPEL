package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.*;
import org.apache.commons.text.similarity.LongestCommonSubsequence;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "lcs")
public class Lcs extends StepStatement implements Serializable {
    private final TemplateLiteral first;
    private final TemplateLiteral second;
    private final String dest;
    private transient LongestCommonSubsequence longestCommonSubsequence;

    @JsonCreator
    public Lcs(@JsonProperty(value = "first", required = true) TemplateLiteral first,
               @JsonProperty(value = "second", required = true) TemplateLiteral second,
               @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.first = first;
        this.second = second;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == longestCommonSubsequence) longestCommonSubsequence = new LongestCommonSubsequence();
        String firstValue = first.get(context);
        String secondValue = second.get(context);
        CharSequence charSequence = longestCommonSubsequence.longestCommonSubsequence(firstValue, secondValue);
        context.addField(dest, charSequence.toString());
        return StepBase.EMPTY;
    }
}
