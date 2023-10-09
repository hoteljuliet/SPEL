package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class DurationTest {
    private final Map<String, Object> backing;
    private final String start;
    private final String end;
    private final String format;
    private final String unit;
    private final String out;
    private final Object expected;

    public DurationTest(Map<String, Object> backing, String start, String end, String format, String unit, String out, Object expected) {
        this.backing = backing;
        this.start = start;
        this.end = end;
        this.format = format;
        this.unit = unit;
        this.out = out;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {

           {Context.mapOf("startTime", "2023-10-09T14:40:37Z", "endTime", "2023-10-09T14:39:37Z"), "startTime", "endTime", "ISO8601", "minutes", "duration", 1l},

        });
    }

    @Test
    public void test() throws Exception {
        Duration duration = new Duration(start, end, format, unit, out);
        Context context = new Context(backing);
        duration.doExecute(context);
        Long actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
