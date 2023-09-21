package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Action;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Pipeline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class B64Test {
    private final Map<String, Object> contextValues;
    private final String source;
    private final String dest;
    private final Action action;
    private final Object expected;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { Context.mapOf("string", "happy days"), "string", "_output.string", Action.ENCODE, "aGFwcHkgZGF5cw=="},
                { Context.mapOf("string", "Z29sZGVuIGdpcmxz"), "string", "_output.string", Action.DECODE, "golden girls"}
        });
    }

    public B64Test(Map<String, Object> contextValues, String source, String dest, Action action, Object expected) {
        this.contextValues = contextValues;
        this.source = source;
        this.dest = dest;
        this.action = action;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(new Pipeline(), contextValues);
        B64 b64 = new B64(source, dest, action);
        b64.doExecute(context);
        assertThat(context.hasField(dest)).isTrue();
        Object added = context.getField(dest);
        assertThat(added).isEqualTo(expected);
    }
}
