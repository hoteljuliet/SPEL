package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mustachejava.Mustache;
import net.hoteljuliet.spel.*;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "render")
public class Render extends StepStatement implements Serializable {
    private final String dest;
    private final String exp;
    private transient Mustache template;

    @JsonCreator
    public Render(@JsonProperty(value = "exp", required = true) String exp,
                  @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.exp = exp;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == template) template = MustacheUtils.compile(String.valueOf(exp));
        String rendered = context.render(template);
        context.addField(dest, rendered);
        return StepBase.EMPTY;
    }
}