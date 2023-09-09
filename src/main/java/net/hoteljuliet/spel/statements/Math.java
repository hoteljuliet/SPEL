package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.primitives.Doubles;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.MathExpression;
import net.hoteljuliet.spel.SpelUtils;
import net.objecthunter.exp4j.Expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Math extends StatementStep {

    // TODO: consider migraating from exp4j to Crunch: https://github.com/Redempt/Crunch
    private String dest;
    private String expression;
    private ThreadLocal<Expression> mathExpression;
    private final Set<String> variables;

    @JsonCreator
    public Math(@JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "exp", required = true) String expression) {
        this.dest = dest;
        this.expression = expression;
        variables = SpelUtils.findVariables(expression);

        String replacedExpression = expression.replaceAll("(\\{\\{|\\}\\})", "");
        mathExpression = MathExpression.get(replacedExpression, variables);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Map<String, Double> variablesMap = new HashMap<>();
        for (String variable : variables) {
            if (!context.hasField(variable)) {
                missing.increment();
            }
            else {
                Object fieldValue = context.getField(variable);
                Double value = Doubles.tryParse(String.valueOf(fieldValue));
                if (value == null) {
                    otherFailure.increment();
                }
                variablesMap.put(variable, value);
            }
        }

        mathExpression.get().setVariables(variablesMap);

        try {
            Double result = mathExpression.get().evaluate();
            context.addField(dest, result);
            success.increment();
        } catch (ArithmeticException ex) {
            handleException(ex);
        }
        return COMMAND_NEITHER;
    }
}