package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import net.objecthunter.exp4j.Expression;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * See https://www.objecthunter.net/exp4j
 */
@Step(tag = "math")
public class Math extends StepStatement implements Serializable {

    private final TemplateLiteral exp;
    private final Set<String> variables;
    private final String dest;
    private transient Expression expression;

    @JsonCreator
    public Math(@JsonProperty(value = "exp", required = true) TemplateLiteral exp,
                @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.exp = exp;
        this.dest = dest;
        this.variables = exp.getVariables();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == expression) {
            expression = io.github.hoteljuliet.spel.predicates.Math.buildExpression(exp, variables);
        }
        Map<String, Double> variablesMap = io.github.hoteljuliet.spel.predicates.Math.resolveVariables(variables, context);
        expression.setVariables(variablesMap);
        context.put(dest, expression.evaluate());
        return EMPTY;
    }
}
