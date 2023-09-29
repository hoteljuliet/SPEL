package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Pipeline;
import io.github.hoteljuliet.spel.Action;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class B64Test {
    private final Map<String, Object> contextValues;
    private final String in;
    private final String out;
    private final Action action;
    private final Object expected;

    public B64Test(Map<String, Object> contextValues, String in, String out, Action action, Object expected) {
        this.contextValues = contextValues;
        this.in = in;
        this.out = out;
        this.action = action;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { Context.mapOf("string", "happy days"), "string", "_output.string", Action.ENCODE, "aGFwcHkgZGF5cw=="},
                { Context.mapOf("string", "Z29sZGVuIGdpcmxz"), "string", "_output.string", Action.DECODE, "golden girls"}
        });
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(new Pipeline(), contextValues);
        B64 b64 = new B64(in, out, action);
        b64.doExecute(context);
        assertThat(context.hasField(out)).isTrue();
        Object added = context.getField(out);
        assertThat(added).isEqualTo(expected);
    }
}
