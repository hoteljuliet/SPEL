package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.FieldType;
import io.github.hoteljuliet.spel.metrics.DefaultMetricsProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HasTypeTest {
    private final Map<String, Object> contextValues;
    private final String in;
    private final FieldType fieldType;
    private final Boolean expected;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Context.mapOf("field", "string"), "field", FieldType.STRING, true},
                {Context.mapOf("field", 1), "field", FieldType.INT, true},
                {Context.mapOf("field", 1l), "field", FieldType.LONG, true},
                {Context.mapOf("field", 1.0f), "field", FieldType.FLOAT, true},
                {Context.mapOf("field", 1.0d), "field", FieldType.DOUBLE, true},
                {Context.mapOf("field", true), "field", FieldType.BOOLEAN, true},
                {Context.mapOf("field", new ArrayList<>()), "field", FieldType.LIST, true},
                {Context.mapOf("field", new HashMap<>()), "field", FieldType.MAP, true},

                {Context.mapOf("field", "string"), "field", FieldType.LONG, false},
                {Context.mapOf("field", "string"), "field", FieldType.DOUBLE, false},
                {Context.mapOf("field", "string"), "field", FieldType.LIST, false},

                {Context.mapOf("field", 1), "field", FieldType.STRING, false},
                {Context.mapOf("field", "string"), "field", FieldType.LONG, false},
                {Context.mapOf("field", "string"), "field", FieldType.DOUBLE, false}

        });
    }

    public HasTypeTest(Map<String, Object> contextValues, String in, FieldType fieldType, Boolean expected) {
        this.contextValues = contextValues;
        this.in = in;
        this.fieldType = fieldType;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        HasType hasType = new HasType(in, fieldType);
        hasType.initMetrics(new DefaultMetricsProvider());
        Optional<Boolean> result = hasType.doExecute(context);
        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expected);
    }
}
