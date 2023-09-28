package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AppendTest {
    private final Map<String, Object> contextValues;
    private final TemplateLiteral value;
    private final String list;
    private final Object expected;

    public AppendTest(Map<String, Object> contextValues, Object value, String list, Object expected) {
        this.contextValues = contextValues;
        this.value = new TemplateLiteral(value);
        this.list = list;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // add a literal to a list
                { Context.mapOf("list", Context.listOf(1, 2, 3)), 4, "list", Context.listOf(1, 2, 3, 4)},
                // add a template to a list
                { Context.mapOf("list", Context.listOf(1, 2, 3), "four", 4), "{{four}}", "list", Context.listOf(1, 2, 3, 4)},
        });
    }

    @Test
    public void test() throws Exception {
        Append append = new Append(value, list);
        Context context = new Context(contextValues);
        append.doExecute(context);
        List actual = context.getField(list);
        assertThat(actual).isEqualTo(expected);
    }
}
