package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.primitives.Doubles;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.statements.StatementStep;
import redempt.crunch.CompiledExpression;

import java.util.List;
import java.util.Optional;

public class Crunch extends PredicateStep {

    private final String expression;
    private final List<String> variables;

    private final CompiledExpression compiledExpression;

    @JsonCreator
    public Crunch(@JsonProperty(value = "exp", required = true) String expression,
                  @JsonProperty(value = "variables", required = true) List<String> variables) {
        this.expression = expression;
        this.variables = variables;
        compiledExpression = redempt.crunch.Crunch.compileExpression(expression);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        double[] vars = new double[variables.size()];

        for (int i = 0; i < vars.length; i++) {
            String variable = variables.get(i);

            if (!context.hasField(variable)) {
                context.missingField(name);
            }
            else {
                Object fieldValue = context.getField(variable);
                Double value = Doubles.tryParse(String.valueOf(fieldValue));
                if (value == null) {
                    context.softFailure(name);
                }
                else {
                    vars[i] = value;
                }
            }
        }

        double result = compiledExpression.evaluate(vars);
        return (result == 1.0) ? COMMAND_TRUE : COMMAND_FALSE;
    }
}