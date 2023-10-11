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
public class SubstringTest {
    private final Map<String, Object> backing;
    private final String in;
    private final String out;
    private final Integer from;
    private final Integer to;
    private final Object expected;

    public SubstringTest(Map<String, Object> backing, String in, String out, Integer from, Integer to, Object expected) {
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
            { Context.mapOf("field", "abracadabra"), "field", "substring", 0, 4, "abra"},
        });
    }

    @Test
    public void test() throws Exception {
        SubString subString = new SubString(in, from, to, out);
        Context context = new Context(backing);
        subString.doExecute(context);
        String actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
