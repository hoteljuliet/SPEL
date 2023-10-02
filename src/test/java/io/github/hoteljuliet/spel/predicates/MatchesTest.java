package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.metrics.DefaultMetricsProvider;
import io.github.hoteljuliet.spel.predicates.HasValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class MatchesTest {
    private final Map<String, Object> contextValues;
    private final String in;
    private final String regex;
    private final Boolean expected;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Context.mapOf("field", "value1"), "field", ".*value1*", true},
                {Context.mapOf("field", "value1"), "field", ".*abc123*", false}
        });
    }

    public MatchesTest(Map<String, Object> contextValues, String in, String regex, Boolean expected) {
        this.contextValues = contextValues;
        this.in = in;
        this.regex = regex;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        Matches matches = new Matches(in, regex);
        matches.initMetrics(new DefaultMetricsProvider());
        Optional<Boolean> result = matches.doExecute(context);
        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expected);
    }
}
