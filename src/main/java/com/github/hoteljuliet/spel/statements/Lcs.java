package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepBase;
import com.github.hoteljuliet.spel.StepStatement;
import com.github.hoteljuliet.spel.*;
import com.github.hoteljuliet.spel.mustache.TemplateLiteral;
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
