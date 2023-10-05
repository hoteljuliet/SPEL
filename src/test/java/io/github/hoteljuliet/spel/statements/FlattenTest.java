package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class FlattenTest {
    private final Map<String, Object> backing;
    private final String in;
    private final String root;
    private final String out;
    private final Object expected;

    public FlattenTest(Map<String, Object> backing, String in, String root, String out, Object expected) {
        this.backing = backing;
        this.in = in;
        this.out = out;
        this.root = root;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
           {Context.mapOf("root", Context.mapOf("fieldA", Context.mapOf("fieldB", "valueB"))), "root", "$", "flattened", Context.mapOf("$.fieldA.fieldB", "valueB")},

           {Context.mapOf("root", Context.mapOf("fieldA", Context.mapOf("fieldB", Context.listOf(0, 1, 2)))), "root", "$", "flattened",
                   Context.mapOf("$.fieldA.fieldB[0]", 0, "$.fieldA.fieldB[1]", 1, "$.fieldA.fieldB[2]", 2)},

        });
    }

    @Test
    public void test() throws Exception {
        Flatten flatten = new Flatten(in, root, out);
        Context context = new Context(backing);
        flatten.doExecute(context);
        Map actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
