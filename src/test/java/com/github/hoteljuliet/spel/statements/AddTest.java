package com.github.hoteljuliet.spel.statements;

import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AddTest {
    private final Map<String, Object> contextValues;
    private final String dest;
    private final Object value;
    private final Object expected;

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
    public AddTest(Map<String, Object> contextValues, String dest, Object value, Object expected) {
        this.contextValues = contextValues;
        this.dest = dest;
        this.value = value;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        Add add = new Add(new TemplateLiteral(value), dest);
        add.doExecute(context);
        assertThat(context.hasField(dest)).isTrue();
        Object added = context.getField(dest);
        assertThat(added).isEqualTo(expected);
    }
}
