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
public class SplitTest {
    private final Map<String, Object> backing;
    private final String in;
    private final String out;
    private final String delimiter;
    private final Object expected;

    public SplitTest(Map<String, Object> backing, String in, String delimiter, String out, Object expected) {
        this.backing = backing;
        this.in = in;
        this.delimiter = delimiter;
        this.out = out;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {

            // Note: split does not strip whitespace, users are expected to use strip before split
            { Context.mapOf("field", "one, two, three, four"), "field", ",", "list", Context.listOf("one", " two", " three", " four")},
            { Context.mapOf("field", "one|two|three|four"), "field", "\\|", "list", Context.listOf("one", "two", "three", "four")}
        });
    }

    @Test
    public void test() throws Exception {
        Split split = new Split(in, delimiter, out);
        Context context = new Context(backing);
        split.doExecute(context);
        List actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
