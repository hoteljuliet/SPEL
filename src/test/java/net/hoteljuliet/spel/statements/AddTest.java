package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.FieldType;
import net.hoteljuliet.spel.TemplateLiteral;
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
    private final String value;

    private final Object expected;
    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Context.mapOf("string", "0"), "addedString", "{{string}}", "0"},
                {Context.mapOf("int", 1), "addedInt", "{{int}}", "1"},
                {Context.mapOf("bool", true), "addedBool", "{{bool}}", "true"},
                {Context.mapOf("long", 5l), "addedLong", "{{long}}", "5"},
                {Context.mapOf("float", 12.06f), "addedFloat", "{{float}}", "12.06"},
                {Context.mapOf("double", 12.06d), "addedDouble", "{{double}}", "12.06"},
                {Context.mapOf("bool1", true, "bool2", true), "concatenated", "{{bool1}}{{bool2}}", "truetrue"}
        });
    }
    public AddMTest(Map<String, Object> contextValues, String dest, String value, Object expected) {
        this.contextValues = contextValues;
        this.dest = dest;
        this.value = value;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        AddM addM = new AddM(new TemplateLiteral(value), dest,FieldType.STRING);
        addM.doExecute(context);
        assertThat(context.hasField(dest)).isTrue();
        Object added = context.getField(dest);
        assertThat(added).isEqualTo(expected);
    }
}
