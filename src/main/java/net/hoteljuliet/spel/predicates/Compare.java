package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.primitives.Doubles;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.MathExpression;
import net.hoteljuliet.spel.Step;
import net.objecthunter.exp4j.Expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Compare extends Step {

    private String expression;
    private ThreadLocal<Expression> mathExpression;
    private final Set<String> variables;

    @JsonCreator
    public Compare(@JsonProperty(value = "exp", required = true) String expression) {
        this.expression = expression;
        variables = findVariables(expression);

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

        Boolean retVal;
        try {
            Double result = mathExpression.get().evaluate();
            retVal = (result == 1) ? true : false;
            success.increment();
        } catch (ArithmeticException ex) {
            otherFailure.increment();
            throw new RuntimeException(ex);
        }
        return Optional.of(retVal);
    }
}