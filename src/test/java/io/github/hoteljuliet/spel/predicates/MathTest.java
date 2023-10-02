package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.metrics.DefaultMetricsProvider;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class MathTest {
    private final Map<String, Object> contextValues;
    private final TemplateLiteral exp;

    private final Boolean expected;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Context.mapOf("f1", 2, "f2", 1), new TemplateLiteral("{{f1}} > {{f2}}"), true},
                {Context.mapOf("f1", 2, "f2", 1), new TemplateLiteral("{{f1}} < {{f2}}"), false},

                {Context.mapOf("f1", 10, "f2", 2), new TemplateLiteral("({{f2}} * 5) == {{f1}}"), true},

                {Context.mapOf("f1", 2.0, "f2", 2.09), new TemplateLiteral("{{f1}} <= {{f2}}"), true},

                {Context.mapOf("f1", 1, "f2", 2, "f3", 3, "f4", 4), new TemplateLiteral("({{f2}} > {{f1}}) && ({{f4}} > {{f3}})"), true},
                {Context.mapOf("f1", 1, "f2", 2, "f3", 3, "f4", 4), new TemplateLiteral("({{f2}} > {{f1}}) || ({{f4}} > {{f3}})"), true},
                {Context.mapOf("f1", 1, "f2", 2, "f3", 3, "f4", 4), new TemplateLiteral("({{f2}} < {{f1}}) && ({{f4}} < {{f3}})"), false},
                {Context.mapOf("f1", 1, "f2", 2, "f3", 3, "f4", 4), new TemplateLiteral("({{f2}} < {{f1}}) || ({{f4}} < {{f3}})"), false},
        });
    }

    public MathTest(Map<String, Object> contextValues, TemplateLiteral exp, Boolean expected) {
        this.contextValues = contextValues;
        this.exp = exp;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        Math math = new Math(exp);
        math.initMetrics(new DefaultMetricsProvider());
        Optional<Boolean> result = math.doExecute(context);
        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expected);
    }
}
