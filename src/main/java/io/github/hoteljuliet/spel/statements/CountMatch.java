package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * given a in and substring, output to out the number of times the substring appears in the value of in
 */
@Step(tag = "count-match")
public class CountMatch extends StepStatement implements Serializable {
    private final TemplateLiteral in;
    private final String out;
    private final TemplateLiteral sub;

    /**
     * 
     * @param in a {@link io.github.hoteljuliet.spel.mustache.TemplateLiteral} to find substrings in
     * @param sub a {@link io.github.hoteljuliet.spel.mustache.TemplateLiteral} that is the substring to search for
     * @param out the path in the context where the substring match count will be placed
     */
    @JsonCreator
    public CountMatch(@JsonProperty(value = "in", required = true) TemplateLiteral in,
                      @JsonProperty(value = "sub", required = true) TemplateLiteral sub,
                      @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.sub = sub;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = in.get(context);
        String subString = sub.get(context);
        context.addField(out, StringUtils.countMatches(value, subString));
        return EMPTY;
    }
}
