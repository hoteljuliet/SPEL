package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.grok.GrokCompiler;
import io.github.hoteljuliet.spel.grok.Match;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Step(tag = "grok")
public class Grok extends StepStatement implements Serializable {
    private final String value;
    private final String dest;
    private final Boolean onlyNamed;
    private final List<String> patterns;

    private final List<io.github.hoteljuliet.spel.grok.Grok> groks;

    @JsonCreator
    public Grok(@JsonProperty(value = "value", required = true) String value,
                @JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "onlyNamed", required = true) Boolean onlyNamed,
                @JsonProperty(value = "patterns", required = true) List<String> patterns) {
        super();
        this.value = value;
        this.dest = dest;
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
        String v = context.getField(value);
        for (io.github.hoteljuliet.spel.grok.Grok grok : groks) {
            Match match = grok.match(v);
            if (match.matches()) {
                Map<String, Object> captures = match.capture();
                for (Map.Entry<String, Object> entry : captures.entrySet()) {
                    context.addField(dest + "." + entry.getKey(), entry.getValue());
                }
                break;
            }
        }
        return EMPTY;
    }
}

