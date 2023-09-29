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
    private final Map<String, Object> backing;
    private final List<TemplateLiteral> in;
    private final String out;
    private final Object expected;

    public AsListTest(Map<String, Object> backing, List<Object> in, String out, Object expected) {
        this.backing = backing;
        this.in = new ArrayList<>();
        for (Object o : in) {
            this.in.add(new TemplateLiteral(o));
        }
        this.out = out;
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
        AsList asList = new AsList(in, out);
        Context context = new Context(backing);
        asList.doExecute(context);
        List actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
