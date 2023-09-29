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
public class AddTest {
    private final Map<String, Object> backing;
    private final String out;
    private final Object in;
    private final Object expected;

    public AddTest(Map<String, Object> backing, String out, Object in, Object expected) {
        this.backing = backing;
        this.out = out;
        this.in = in;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // add literals
                {Context.mapOf(), "addedString", "test", "test"},
                {Context.mapOf(), "addedInt", 1, 1},
                {Context.mapOf(), "addedBool", true, true},
                {Context.mapOf(), "addedLong", 5l,  5l},
                {Context.mapOf(), "addedFloat", 12.06f, 12.06f},
                {Context.mapOf(), "addedDouble", 12.06d,  12.06d},
                // add via templates
                {Context.mapOf("string", "test"), "addedString", "{{string}}", "test"},
                {Context.mapOf("int", 1), "addedInt", "{{int}}", 1},
                {Context.mapOf("bool", true), "addedBool", "{{bool}}", true},
                {Context.mapOf("long", 5l), "addedLong", "{{long}}", 5l},
                {Context.mapOf("float", 12.06f), "addedFloat", "{{float}}", 12.06f},
                {Context.mapOf("double", 12.06d), "addedDouble", "{{double}}", 12.06d}
        });
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(backing);
        Add add = new Add(new TemplateLiteral(in), out);
        add.doExecute(context);
        assertThat(context.hasField(out)).isTrue();
        Object added = context.getField(out);
        assertThat(added).isEqualTo(expected);
    }
}
