package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AsListTest {
    private final Map<String, Object> contextValues;
    private final List<TemplateLiteral> values;
    private final String list;
    private final Object expected;

    public AsListTest(Map<String, Object> contextValues, List<Object> values, String list, Object expected) {
        this.contextValues = contextValues;
        this.values = new ArrayList<>();
        for (Object o : values) {
            this.values.add(new TemplateLiteral(o));
        }
        this.list = list;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { Context.mapOf("one", 1, "two", 2, "three", 3), Context.listOf("{{one}}", "{{two}}", "{{three}}"), "list", Context.listOf(1, 2, 3)},
        });
    }

    @Test
    public void test() throws Exception {
        AsList asList = new AsList(values, list);
        Context context = new Context(contextValues);
        asList.doExecute(context);
        List actual = context.getField(list);
        assertThat(actual).isEqualTo(expected);
    }
}
