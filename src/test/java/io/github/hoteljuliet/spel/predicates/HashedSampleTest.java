package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.metrics.DefaultMetricsProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HashedSampleTest {
    private final Map<String, Object> contextValues;
    private final String in;
    private final Integer pct;
    private final Boolean expected;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Context.mapOf("field", "value1"), 100, "field", true},
                {Context.mapOf("field", "value1"), 0, "field", false}
        });
    }

    public HashedSampleTest(Map<String, Object> contextValues, Integer pct, String in, Boolean expected) {
        this.contextValues = contextValues;
        this.in = in;
        this.pct = pct;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        HashedSample hashedSample = new HashedSample(pct, in);
        hashedSample.initMetrics(new DefaultMetricsProvider());
        Optional<Boolean> result = hashedSample.doExecute(context);
        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expected);
    }

    @Test
    public void test_50() throws Exception {
        int numTrue = 0;
        for (int i = 0; i < 100; i++) {
            Context context = new Context(Context.mapOf("field", RandomStringUtils.randomAlphabetic(16)));
            HashedSample hashedSample = new HashedSample(50, "field");
            hashedSample.initMetrics(new DefaultMetricsProvider());
            Optional<Boolean> result = hashedSample.doExecute(context);
            if (result.get()) {
                numTrue += 1;
            }
        }
        assertThat(numTrue).isGreaterThan(40).isLessThanOrEqualTo(60);
    }
}
