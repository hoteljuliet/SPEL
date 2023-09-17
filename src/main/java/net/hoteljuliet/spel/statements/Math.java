package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.primitives.Doubles;
import net.hoteljuliet.spel.*;
import net.objecthunter.exp4j.Expression;

import java.io.Serializable;
import java.util.*;

// TODO: keep for now, but consider removing and just using crunch instead (crunch is faster)
@Step(tag = "math")
public class Math extends StepStatement implements Serializable {

    private String dest;
    private String expression;
    private ThreadLocal<Expression> mathExpression;
    private final List<String> variables;

    @JsonCreator
    public Math(@JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "exp", required = true) String expression) {
        super();
        this.dest = dest;
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
                missingField();
            }
            else {
                Object fieldValue = context.getField(variable);
                Double value = Doubles.tryParse(String.valueOf(fieldValue));
                if (value == null) {
                    softFailure();
                }
                else {
                    variablesMap.put(variable, value);
                }
            }
        }
        mathExpression.get().setVariables(variablesMap);
        Double result = mathExpression.get().evaluate();
        context.addField(dest, result);
        return NEITHER;
    }
}