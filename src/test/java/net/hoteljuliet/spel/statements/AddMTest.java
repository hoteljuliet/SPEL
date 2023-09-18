package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AddMTest {
    private final Map<String, Object> contextValues;
    private final String dest;
    private final String expression;

    private final Object expected;
    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Context.mapOf("string", "0"), "_input.addedString", "{{_input.string}}", "0"},
                {Context.mapOf("int", 1), "_input.addedInt", "{{_input.int}}", "1"},
                {Context.mapOf("bool", true), "_input.addedBool", "{{_input.bool}}", "true"},
                {Context.mapOf("long", 5l), "_input.addedLong", "{{_input.long}}", "5"},
                {Context.mapOf("float", 12.06f), "_input.addedFloat", "{{_input.float}}", "12.06"},
                {Context.mapOf("double", 12.06d), "_input.addedDouble", "{{_input.double}}", "12.06"},
                {Context.mapOf("bool1", true, "bool2", true), "_input.concatenated", "{{_input.bool1}}{{_input.bool2}}", "truetrue"}
        });
    }
    public AddMTest(Map<String, Object> contextValues, String dest, String expression, Object expected) {
        this.contextValues = contextValues;
        this.dest = dest;
        this.expression = expression;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        AddM addM = new AddM(dest, expression);
        addM.doExecute(context);
        assertThat(context.hasField(dest)).isTrue();
        Object added = context.getField(dest);
        assertThat(added).isEqualTo(expected);
    }
}
