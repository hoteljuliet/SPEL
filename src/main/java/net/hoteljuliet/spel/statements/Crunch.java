package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.primitives.Doubles;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;
import redempt.crunch.CompiledExpression;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "crunch")
public class Crunch extends StepStatement implements Serializable {

    private final String dest;
    private final String expression;
    private final List<String> variables;
    private transient CompiledExpression compiledExpression;

    @JsonCreator
    public Crunch(@JsonProperty(value = "dest", required = true) String dest,
                  @JsonProperty(value = "exp", required = true) String expression,
                  @JsonProperty(value = "variables", required = true) List<String> variables) {
        super();
        this.dest = dest;
        this.expression = expression;
        this.variables = variables;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (null == compiledExpression) compiledExpression = redempt.crunch.Crunch.compileExpression(expression);

        double[] vars = new double[variables.size()];

        for (int i = 0; i < vars.length; i++) {
            String variable = variables.get(i);
            Object fieldValue = context.getField(variable);
            Double value = Doubles.tryParse(String.valueOf(fieldValue));
            vars[i] = value;
        }

        double result = compiledExpression.evaluate(vars);
        context.addField(dest, result);
        return EMPTY;
    }
}