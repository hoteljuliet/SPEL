package net.hoteljuliet.spel;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

import java.util.Set;

// TODO: consider migrating from exp4j to Crunch: https://github.com/Redempt/Crunch

public class MathExpression {

    public static ThreadLocal<Expression> get(String expression, Set<String> variables) {

        // TODO: add more custom operators
        Operator gte = new Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            @Override
            public double apply(double[] values) {
                if (values[0] >= values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }
            }
        };

        Operator gt = new Operator(">", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            @Override
            public double apply(double[] values) {
                if (values[0] > values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }
            }
        };

        Operator lte = new Operator("<=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            @Override
            public double apply(double[] values) {
                if (values[0] <= values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }
            }
        };

        Operator lt = new Operator("<", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            @Override
            public double apply(double[] values) {
                if (values[0] < values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }
            }
        };

        Operator eq = new Operator("==", 2, true, Operator.PRECEDENCE_ADDITION - 1) {
            @Override
            public double apply(double[] values) {
                if (values[0] == values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }
            }
        };

        return ThreadLocal.withInitial(() -> {
            try {
                return new ExpressionBuilder(expression).variables(variables).operator(lte, lt, gte, gt, eq).build();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format("invalid expression [%s]", expression));
            }
        });
    }
}
