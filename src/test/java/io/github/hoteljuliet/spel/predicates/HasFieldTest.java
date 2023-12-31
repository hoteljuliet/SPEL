package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.metrics.DefaultMetricsProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HasFieldTest {
    private final Map<String, Object> contextValues;
    private final String in;
    private final Boolean expected;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Context.mapOf("field1", "value1"), "field1", true},
                {Context.mapOf("field1", "value1"), "field2", false},
        });
    }

    public HasFieldTest(Map<String, Object> contextValues, String in, Boolean expected) {
        this.contextValues = contextValues;
        this.in = in;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        HasField hasField = new HasField(in);
        Optional<Boolean> result = hasField.doExecute(context);
        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expected);
    }
}
