package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SubListTest {
    private final Map<String, Object> backing;
    private final String in;
    private final String out;
    private final Integer from;
    private final Integer to;
    private final Object expected;

    public SubListTest(Map<String, Object> backing, String in, String out, Integer from, Integer to, Object expected) {
        this.backing = backing;
        this.in = in;
        this.out = out;
        this.from = from;
        this.to = to;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { Context.mapOf("field", Context.listOf(1, 2, 3, 4, 5)), "field", "sublist", 0, 3, Context.listOf(1, 2, 3)},
        });
    }

    @Test
    public void test() throws Exception {
        SubList subList = new SubList(in, from, to, out);
        Context context = new Context(backing);
        subList.doExecute(context);
        List actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
