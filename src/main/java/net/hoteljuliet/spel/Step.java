package net.hoteljuliet.spel;

import com.github.mustachejava.MustacheFactory;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Step implements Command {

    private final static Pattern mustachePattern = Pattern.compile("\\{\\{(.+?)\\}\\}");

    protected MustacheFactory mustacheFactory = new UnescapedMustacheFactory();
    protected org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();

    public LongAdder success = new LongAdder();
    public LongAdder missing = new LongAdder();
    public LongAdder exceptionThrown = new LongAdder();
    public LongAdder overTime = new LongAdder();
    public LongAdder otherFailure = new LongAdder();

    public LongAdder nanos = new LongAdder();

    public LongAdder evalTrue = new LongAdder();
    public LongAdder evalFalse = new LongAdder();

    public ThreadLocal<Expression> getMathExpression(String expression, Set<String> variables) {

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
                return new ExpressionBuilder(expression)
                        .variables(variables)
                        .operator(lte, lt, gte, gt, eq)
                        .build();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format("invalid expression [%s]", expression));
            }
        });
    }

    public Set<String> findVariables(String expression) {
        Set<String> variables = new HashSet<>();
        Matcher matcher = mustachePattern.matcher(expression);

        while (matcher.find()) {
            variables.add(matcher.group(1));
        }

        return variables;
    }

}
