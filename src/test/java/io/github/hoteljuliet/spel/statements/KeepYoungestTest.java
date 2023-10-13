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
public class KeepYoungestTest {
    private final List<Map<String, Object>> backing;
    private final String in;
    private final String eventTime;
    private final String now;
    private final Long max;
    private final String out;
    private final Object expected;

    public KeepYoungestTest(List<Map<String, Object>> backing, String in, String eventTime, String now, Long max, String out, Object expected) {
        this.backing = backing;
        this.in = in;
        this.eventTime = eventTime;
        this.now = now;
        this.max = max;
        this.out = out;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {

            {Context.listOf(
           Context.mapOf("field", Context.mapOf("a", 1), "eventTime", System.currentTimeMillis() - 1000, "now", System.currentTimeMillis()),
                    Context.mapOf("field", Context.mapOf("b", 2), "eventTime", System.currentTimeMillis() - 2000, "now", System.currentTimeMillis()),
                    Context.mapOf("field", Context.mapOf("c", 3), "eventTime", System.currentTimeMillis() - 3000, "now", System.currentTimeMillis()),
                    Context.mapOf("field", Context.mapOf("d", 4), "eventTime", System.currentTimeMillis() - 4000, "now", System.currentTimeMillis()),
                    Context.mapOf("field", Context.mapOf("e", 5), "eventTime", System.currentTimeMillis() - 5000,  "now", System.currentTimeMillis())
            ),
            "field", "eventTime", "now", 4000l, "keptValues",
            Context.listOf(Context.mapOf("b", 2), Context.mapOf("a", 1))},
        });
    }

    @Test
    public void test() throws Exception {
        KeepYoungest keepYoungest = new KeepYoungest(in, eventTime, now, max, out);
        Context context = null;
        for (Map<String, Object> map : backing) {
            context = new Context(map);
            keepYoungest.doExecute(context);
        }

        List actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testEvictions() throws Exception {
        KeepYoungest keepYoungest = new KeepYoungest(in, eventTime, now, max, out);
        Context context = null;
        for (Map<String, Object> map : backing) {
            Long nowValue = (Long) map.get("now");
            map.put("now", nowValue + 1000);

            context = new Context(map);
            keepYoungest.doExecute(context);
        }

        List actual = context.getField(out);
        assertThat(actual).isEqualTo(Context.listOf(Context.mapOf("b", 2), Context.mapOf("a", 1)));
    }

}
