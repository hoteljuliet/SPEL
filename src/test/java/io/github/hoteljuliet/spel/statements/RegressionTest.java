package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class RegressionTest {
    private final List<Map<String, Object>> backing;
    private final String x;
    private final String y;
    private final TemplateLiteral predict;
    private final String out;
    private final Object expected;

    public RegressionTest(List<Map<String, Object>> backing, String x, String y, TemplateLiteral predict, String out, Object expected) {
        this.backing = backing;
        this.x = x;
        this.y = y;
        this.predict = predict;
        this.out = out;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { Context.listOf(
                Context.mapOf("x", 1.0, "y", 1.0),
                Context.mapOf("x", 2.0, "y", 2.0),
                Context.mapOf("x", 3.0, "y", 3.0)
            ), "x", "y", new TemplateLiteral(10.0), "prediction", Context.mapOf("intercept", 0.0, "slope", 1.0, "slopeStdErr", 0.0, "predictedY", 10.0)},
        });
    }

    @Test
    public void test() throws Exception {
        Regression regression = new Regression(x, y, predict, out);
        Context context = new Context();
        for (Map<String, Object> map : backing) {
            context.putAll(map);
            regression.doExecute(context);
        }
        Map actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
