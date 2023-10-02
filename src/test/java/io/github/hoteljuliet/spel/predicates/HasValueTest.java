package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.metrics.DefaultMetricsProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HasValueTest {
    private final Map<String, Object> contextValues;
    private final String in;
    private final List<Object> values;
    private final Boolean expected;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Context.mapOf("field", "value1"), "field", Arrays.asList("value1"), true},
                {Context.mapOf("field", "value1"), "field", Arrays.asList("someOtherValue"), false},

                {Context.mapOf("field", 1), "field", Arrays.asList(1, 2, 3), true},
                {Context.mapOf("field", 2.0), "field", Arrays.asList(1.0, 3.0), false},

                {Context.mapOf("field", true), "field", Arrays.asList(true), true},
                {Context.mapOf("field", false), "field", Arrays.asList(true), false},
        });
    }

    public HasValueTest(Map<String, Object> contextValues, String in, List<Object> values, Boolean expected) {
        this.contextValues = contextValues;
        this.in = in;
        this.values = values;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        HasValue hasValue = new HasValue(in, values);
        hasValue.initMetrics(new DefaultMetricsProvider());
        Optional<Boolean> result = hasValue.doExecute(context);
        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expected);
    }
}
