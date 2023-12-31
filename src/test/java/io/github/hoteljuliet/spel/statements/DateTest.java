package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class DateTest {
    private final Map<String, Object> backing;
    private final String in;
    private final String from;
    private final String to;
    private final String fromZone;
    private final String toZone;
    private final String out;
    private final Object expected;

    public DateTest(Map<String, Object> backing, String in, String from, String to, String fromZone, String toZone, String out, Object expected) {
        this.backing = backing;
        this.in = in;
        this.out = out;
        this.from = from;
        this.to = to;
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
           {Context.mapOf("field", "2023-10-09T14:40:37Z"), "field", "ISO8601", "MM-dd-YYYY", null, "UTC", "reformatted", "10-09-2023"},
        });
    }

    @Test
    public void test() throws Exception {
        Date date = new Date(in, from, to, fromZone, toZone, out);
        Context context = new Context(backing);
        date.doExecute(context);
        String actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
