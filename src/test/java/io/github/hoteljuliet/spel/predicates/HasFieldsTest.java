package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HasFieldsTest {
    private final Map<String, Object> contextValues;
    private final List<String> in;
    private final Boolean expected;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Context.mapOf("field1", "value1", "field2", "value2"), Context.listOf("field1", "field2"), true},
                {Context.mapOf("field1", "value1"), Context.listOf("field1", "field2"), false},
        });
    }

    public HasFieldsTest(Map<String, Object> contextValues, List<String> in, Boolean expected) {
        this.contextValues = contextValues;
        this.in = in;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        HasFields hasFields = new HasFields(in);
        Optional<Boolean> result = hasFields.doExecute(context);
        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expected);
    }
}
