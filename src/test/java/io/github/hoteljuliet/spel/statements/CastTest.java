package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.FieldType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class CastTest {
    private final Map<String, Object> backing;
    private final String in;
    private final FieldType fieldType;
    private final Object expected;

    public CastTest(Map<String, Object> backing, String in, FieldType fieldType, Object expected) {
        this.backing = backing;
        this.in = in;
        this.fieldType = fieldType;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { Context.mapOf("field", "1"), "field", FieldType.INT, 1},
            { Context.mapOf("field", "1"), "field", FieldType.LONG, 1l},
            { Context.mapOf("field", "1"), "field", FieldType.FLOAT, 1f},
            { Context.mapOf("field", "1"), "field", FieldType.DOUBLE, 1d},
            { Context.mapOf("field", 1), "field", FieldType.STRING, "1"},
            { Context.mapOf("field", 2.0), "field", FieldType.STRING, "2.0"},
        });
    }

    @Test
    public void test() throws Exception {
        Cast cast = new Cast(in, fieldType);
        Context context = new Context(backing);
        cast.doExecute(context);
        Object actual = context.getField(in);
        assertThat(fieldType.isInstance(actual)).isTrue();
        assertThat(actual).isEqualTo(expected);
    }
}
