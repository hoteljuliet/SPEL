package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class RenderTest {
    private final Map<String, Object> backing;
    private final String exp;
    private final String out;
    private final Object expected;

    public RenderTest(Map<String, Object> backing, String exp, String out, Object expected) {
        this.backing = backing;
        this.exp = exp;
        this.out = out;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {Context.mapOf("fieldA", "A", "fieldB", "B"), "{{fieldA}}_{{fieldB}}", "out", "A_B"},
        });
    }

    @Test
    public void test() throws Exception {
        Render render = new Render(exp, out);
        Context context = new Context(backing);
        render.doExecute(context);
        String actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
