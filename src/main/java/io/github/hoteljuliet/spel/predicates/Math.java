package io.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepPredicate;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Step(tag = "math")
public class Math extends StepPredicate implements Serializable {
    private final TemplateLiteral exp;
    private final Set<String> variables;
    private transient Expression expression;

    @JsonCreator
    public Math(@JsonProperty(value = "exp", required = true) TemplateLiteral exp) {
        super();
        this.exp = exp;
        this.variables = exp.getVariables();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == expression) {
            expression = buildExpression(exp, variables);
        }
        Map<String, Double> variablesMap = resolveVariables(variables, context);
        expression.setVariables(variablesMap);
        Double result = expression.evaluate();
        return (result == 1.0) ? TRUE : FALSE;
    }

    public static Map<String, Double> resolveVariables(Set<String> variables, Context context) {
        Map<String, Double> variablesMap = new HashMap<>();
        for (String variable : variables) {
            Object value = context.getField(variable);

            if (value instanceof Number) {
                variablesMap.put(variable, ((Number)value).doubleValue());
            }
            else if (value instanceof Boolean) {
                Boolean b = (Boolean) value;
                if (b) {
                    variablesMap.put(variable, 1.0);
                }
                else {
                    variablesMap.put(variable, 0.0);
                }
            }
            else if (value instanceof String) {
                String s = (String) value;
                if (s.matches("^(t|true|yes|y|1)$")) {
                    variablesMap.put(variable, 1.0);
                }
                else {
                    variablesMap.put(variable, 0.0);
                }
            }
        }
        return variablesMap;
    }

    public static Expression buildExpression(TemplateLiteral exp, Set<String> variables) {

        Operator or = new Operator("||", 2, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                Boolean left = args[0] == 1.0 ? true : false;
                Boolean right = args[0] == 1.0 ? true : false;
                return (left || right) ? 1.0 : 0.0;
            }
        };

        Operator and = new Operator("&&", 2, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                Boolean left = args[0] == 1.0 ? true : false;
                Boolean right = args[0] == 1.0 ? true : false;
                return (left && right) ? 1.0 : 0.0;
            }
        };

        Operator gt = new Operator(">", 2, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                double left = args[0];
                double right = args[1];
                return (left > right) ? 1.0 : 0.0;
            }
        };

        Operator gte = new Operator(">=", 2, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                double left = args[0];
                double right = args[1];
                return (left >= right) ? 1.0 : 0.0;
            }
        };

        Operator lt = new Operator("<", 2, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                double left = args[0];
                double right = args[1];
                return (left < right) ? 1.0 : 0.0;
            }
        };

        Operator lte = new Operator("<=", 2, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                double left = args[0];
                double right = args[1];
                return (left <= right) ? 1.0 : 0.0;
            }
        };

        Operator eq = new Operator("==", 2, true, Operator.PRECEDENCE_POWER + 1) {
            @Override
            public double apply(double... args) {
                double left = args[0];
                double right = args[1];
                return (left == right) ? 1.0 : 0.0;
            }
        };

        return new ExpressionBuilder(exp.getTrimmed())
                .operator(or, and, lt, lte, gt, gte, eq)
                .variables(variables)
                .build();
    }
}
