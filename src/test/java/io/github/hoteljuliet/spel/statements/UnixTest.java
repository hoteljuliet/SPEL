package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class UnixTest {
    private final Map<String, Object> backing;
    private final String in;
    private final String out;
    private final String from;
    private final String to;
    private final String fromZone;
    private final String toZone;
    private final Object expected;

    public UnixTest(Map<String, Object> backing, String in, String from, String to, String fromZone, String toZone, String out, Object expected) {
        this.backing = backing;
        this.in = in;
        this.from = from;
        this.to = to;
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.expected = expected;
        this.out = out;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
           {Context.mapOf("field", 1697033148l), "field", "UNIX_S", "MM-dd-YYYY", "UTC", null, "reformatted", "10-11-2023"},
           {Context.mapOf("field", 1697033148), "field", "UNIX_S", "MM-dd-YYYY", "UTC", "UTC", "reformatted", "10-11-2023"},
        });
    }

    @Test
    public void test() throws Exception {
        Unix unix = new Unix(in, from, to, fromZone, toZone, out);
        Context context = new Context(backing);
        unix.doExecute(context);
        String actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
