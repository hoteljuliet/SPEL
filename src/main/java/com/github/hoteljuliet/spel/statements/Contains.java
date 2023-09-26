package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepStatement;
import com.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * given a source and substring, output to dest the number of times the substring appears in the value of source
 */
@Step(tag = "contains")
public class Contains extends StepStatement implements Serializable {
    private final TemplateLiteral source;
    private final String dest;
    private final TemplateLiteral sub;

    @JsonCreator
    public Contains(@JsonProperty(value = "source", required = true) TemplateLiteral source,
                    @JsonProperty(value = "sub", required = true) TemplateLiteral sub,
                    @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.sub = sub;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = source.get(context);
        String subString = sub.get(context);
        context.addField(dest, StringUtils.countMatches(value, subString));
        return EMPTY;
    }
}
