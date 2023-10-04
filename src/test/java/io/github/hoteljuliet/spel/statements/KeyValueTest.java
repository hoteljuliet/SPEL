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
public class KeyValueTest {
    private final Map<String, Object> backing;
    private final String in;
    private final String out;
    private final String delimiter;
    private final String separator;
    private final Object expected;

    public KeyValueTest(Map<String, Object> backing, String in, String delimiter, String separator, String out, Object expected) {
        this.backing = backing;
        this.in = in;
        this.delimiter = delimiter;
        this.separator = separator;
        this.out = out;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            // Note: split does not strip whitespace, users are expected to use strip before split
            { Context.mapOf("field", "k1=v1, k2=v2, k3=v3"), "field", ",", "=", "map", Context.mapOf("k1", "v1", " k2", "v2", " k3", "v3")},

            { Context.mapOf("field", "k1:v1|k2:v2|k3:v3"), "field", "\\|", ":", "map", Context.mapOf("k1", "v1", "k2", "v2", "k3", "v3")},
        });
    }

    @Test
    public void test() throws Exception {
        KeyValue keyValue = new KeyValue(in, delimiter, separator, out);
        Context context = new Context(backing);
        keyValue.doExecute(context);
        Map actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
