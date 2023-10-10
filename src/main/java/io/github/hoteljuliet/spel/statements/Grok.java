package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.grok.GrokCompiler;
import io.github.hoteljuliet.spel.grok.Match;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Step(tag = "grok")
public class Grok extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final Boolean onlyNamed;
    private final List<String> patterns;

    private final List<io.github.hoteljuliet.spel.grok.Grok> groks;

    /**
     *
     * @param in a path to a String value in the context
     * @param out the path in the Context where the resulting Map will be placed
     * @param onlyNamed if true, only fields named in the pattern will be output
     * @param patterns grok patterns to match against, See https://www.elastic.co/guide/en/logstash/current/plugins-filters-grok.html
     */
    @JsonCreator
    public Grok(@JsonProperty(value = "in", required = true) String in,
                @JsonProperty(value = "out", required = true) String out,
                @JsonProperty(value = "onlyNamed", required = true) Boolean onlyNamed,
                @JsonProperty(value = "patterns", required = true) List<String> patterns) {
        super();
        this.in = in;
        this.out = out;
        this.onlyNamed = onlyNamed;
        this.patterns = patterns;

        groks = new ArrayList<>();
        for (String pattern : patterns) {
            io.github.hoteljuliet.spel.grok.Grok grok = GrokCompiler.getInstance().compile(pattern, onlyNamed);
            groks.add(grok);
        }
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String v = context.getField(in);
        for (io.github.hoteljuliet.spel.grok.Grok grok : this.groks) {
            Match match = grok.match(v);
            if (match.matches()) {
                Map<String, Object> captures = match.capture();
                for (Map.Entry<String, Object> entry : captures.entrySet()) {
                    context.addField(out + "." + entry.getKey(), entry.getValue());
                }
                break;
            }
        }
        return EMPTY;
    }
}

