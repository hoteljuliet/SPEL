package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import net.hoteljuliet.spel.*;
import net.hoteljuliet.spel.mustache.UnescapedMustacheFactory;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Optional;

/**
 * Renders a new field with full mustache support.
 */
@Step(tag = "render")
public class Render extends StepStatement implements Serializable {
    private final static MustacheFactory mustacheFactory = new UnescapedMustacheFactory();
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
        if (null == template) template = compile(String.valueOf(exp));
        String rendered = context.render(template);
        context.addField(dest, rendered);
        return StepBase.EMPTY;
    }


    public Mustache compile(String exp) {
        return mustacheFactory.compile(new StringReader(exp), "");
    }
}