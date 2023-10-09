package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class IntersectionTest {
    private final Map<String, Object> backing;
    private final TemplateLiteral first;
    private final TemplateLiteral second;
    private final String out;
    private final Object expected;

    public IntersectionTest(Map<String, Object> backing, TemplateLiteral first, TemplateLiteral second, String out, Object expected) {
        this.backing = backing;
        this.first = first;
        this.second = second;
        this.out = out;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {Context.mapOf("field", Context.listOf(1, 2, 3, 4, 5)),
                    new TemplateLiteral("{{field}}"), new TemplateLiteral(Context.listOf(2, 3, 4)),
                    "intersection", Context.listOf(2, 3, 4)},


            {Context.mapOf("field", Context.listOf(Context.mapOf("v1", "k1"), Context.mapOf("v2", "k2"))),
                    new TemplateLiteral("{{field}}"), new TemplateLiteral(Context.listOf(Context.mapOf("v2", "k2"))),
                    "intersection", Context.listOf(Context.mapOf("v2", "k2"))},

        });
    }

    @Test
    public void test() throws Exception {
        Intersection intersection = new Intersection(first, second, out);
        Context context = new Context(backing);
        intersection.doExecute(context);
        List actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
