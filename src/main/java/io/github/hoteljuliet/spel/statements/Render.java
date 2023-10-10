package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepBase;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.mustache.UnescapedMustacheFactory;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Optional;

/**
 * Renders a new field with full mustache support.
 */
@Step(tag = "render")
public class Render extends StepStatement implements Serializable {
    private final static MustacheFactory mustacheFactory = new UnescapedMustacheFactory();
    private final String out;
    private final String exp;
    private transient Mustache template;

    /**
     * Create a new string from multiple values in the Context
     * @param exp a "mustache" expression of paths to use in the creation of a new string.
     * @param out the path in the context the new String will be placed.
     */
    @JsonCreator
    public Render(@JsonProperty(value = "exp", required = true) String exp,
                  @JsonProperty(value = "out", required = true) String out) {
        super();
        this.exp = exp;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == template) template = compile(String.valueOf(exp));
        String rendered = context.render(template);
        context.addField(out, rendered);
        return StepBase.EMPTY;
    }


    public Mustache compile(String exp) {
        return mustacheFactory.compile(new StringReader(exp), "");
    }
}