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
public class StatsTest {
    private final List<Map<String, Object>> backing;
    private final String in;
    private final String out;
    private final Object expected;

    public StatsTest(List<Map<String, Object>> backing, String in, String out, Object expected) {
        this.backing = backing;
        this.in = in;
        this.out = out;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {

            {Context.listOf(
                    Context.mapOf("field", 1.0),
                    Context.mapOf("field", 2.0),
                    Context.mapOf("field", 3.0),
                    Context.mapOf("field", 4.0),
                    Context.mapOf("field", 5.0)
            ),
            "field", "stats",
            Context.mapOf("mean", 3.0, "min", 1.0, "max", 5.0, "variance", 2.5, "sigma", 1.5811388300841898, "sum", 15.0)},
        });
    }

    @Test
    public void test() throws Exception {
        Stats stats = new Stats(in, out);
        Context context = null;
        for (Map<String, Object> map : backing) {
            context = new Context(map);
            stats.doExecute(context);
        }

        Map actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
