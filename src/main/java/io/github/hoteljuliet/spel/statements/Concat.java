package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Concatenates multiple strings into one, separated by a delimiter. The opposite of split.
 */
@Step(tag = "concat")
public class Concat extends StepStatement implements Serializable {

    private final List<TemplateLiteral> in;
    private final String delimiter;
    private final String out;

    /**
     * Concatenates multiple strings into one, separated by a delimiter. The opposite of split.
     * @param in a List of {@link io.github.hoteljuliet.spel.mustache.TemplateLiteral} to create the concatenated String from.
     * @param out the path in the context where the concatenated String will be placed
     * @param delimiter
     */
    @JsonCreator
    public Concat(@JsonProperty(value = "in", required = true) List<TemplateLiteral> in,
                  @JsonProperty(value = "out", required = true) String out,
                  @JsonProperty(value = "delimiter", required = true) String delimiter) {
        super();
        this.in = in;
        this.delimiter = delimiter;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        List<String> strings = new ArrayList<>();

        for (TemplateLiteral templateLiteral : in) {
            String value = templateLiteral.get(context);
            strings.add(value);
        }
        context.addField(out, Joiner.on(delimiter).join(strings));
        return EMPTY;
    }
}

