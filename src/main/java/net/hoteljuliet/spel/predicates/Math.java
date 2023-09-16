package net.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.primitives.Doubles;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.MathExpression;
import net.hoteljuliet.spel.SpelUtils;
import net.hoteljuliet.spel.Step;
import net.objecthunter.exp4j.Expression;

import java.io.Serializable;
import java.util.*;

// TODO: keep for now, but consider removing and just using crunch instead (crunch is faster)
@Step(tag = "math")
public class Math extends PredicateBaseStep implements Serializable {

    private String expression;
    private ThreadLocal<Expression> mathExpression;
    private final List<String> variables;

    @JsonCreator
    public Math(@JsonProperty(value = "exp", required = true) String expression) {
        super();
        this.expression = expression;
        variables = SpelUtils.findVariables(expression);

        String replacedExpression = expression.replaceAll("(\\{\\{|\\}\\})", "");
        mathExpression = MathExpression.get(replacedExpression, new HashSet<>(variables));
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Map<String, Double> variablesMap = new HashMap<>();
        for (String variable : variables) {
            if (!context.hasField(variable)) {
                missingField.increment();
            }
            else {
                Object fieldValue = context.getField(variable);
                Double value = Doubles.tryParse(String.valueOf(fieldValue));
                if (value == null) {
                    softFailure.increment();
                }
                else {
                    variablesMap.put(variable, value);
                }
            }
        }

        mathExpression.get().setVariables(variablesMap);
        Double result = mathExpression.get().evaluate();
        return (result == 1.0) ? TRUE : FALSE;
    }
}