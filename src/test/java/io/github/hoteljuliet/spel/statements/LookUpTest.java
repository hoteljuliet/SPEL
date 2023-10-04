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
public class LookUpTest {
    private final Map<String, Object> backing;
    private final String in;
    private final String out;
    private final String defaultValue;
    private final String dict;
    private final Object expected;

    public LookUpTest(Map<String, Object> backing, String in, String out, String defaultValue, String dict, Object expected) {
        this.backing = backing;
        this.in = in;
        this.defaultValue = defaultValue;
        this.dict = dict;
        this.out = out;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { Context.mapOf("field", "200", "dict", Context.mapOf(200, "OK", 404, "NOT FOUND", 418, "IM A TEAPOT", 500, "SERVER ERROR")), "field", "lookedUp", "not found", "dict", "OK"}
        });
    }

    @Test
    public void test() throws Exception {
        LookUp lookUp = new LookUp(in, out, defaultValue, dict);
        Context context = new Context(backing);
        lookUp.doExecute(context);
        String actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
