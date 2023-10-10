package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ReplaceTest {
    private final Map<String, Object> backing;
    private final String in;
    private final TemplateLiteral from;
    private final TemplateLiteral to;
    private final Object expected;

    public ReplaceTest(Map<String, Object> backing, String in, TemplateLiteral from, TemplateLiteral to, Object expected) {
        this.backing = backing;
        this.in = in;
        this.from = from;
        this.to = to;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { Context.mapOf("field", "abracadabra"), "field", new TemplateLiteral("bra"), new TemplateLiteral("bbb"), "abbbcadabbb"}
        });
    }

    @Test
    public void test() throws Exception {
        Replace replace = new Replace(in, from, to);
        Context context = new Context(backing);
        replace.doExecute(context);
        String actual = context.getField(in);
        assertThat(actual).isEqualTo(expected);
    }
}
