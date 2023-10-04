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
public class StripTest {
    private final Map<String, Object> backing;
    private final String in;
    private final Object expected;

    public StripTest(Map<String, Object> backing, String in, Object expected) {
        this.backing = backing;
        this.in = in;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            // add a literal to a list
            { Context.mapOf("field", "      white spaces must     go    "), "field", "whitespacesmustgo"}
        });
    }

    @Test
    public void test() throws Exception {
        Strip strip = new Strip(in);
        Context context = new Context(backing);
        strip.doExecute(context);
        String actual = context.getField(in);
        assertThat(actual).isEqualTo(expected);
    }
}
