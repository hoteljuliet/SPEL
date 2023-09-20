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
public class AddTest {
    private final Map<String, Object> contextValues;
    private final String dest;
    private final Object value;
    private final FieldType fieldType;

    private final Object expected;
    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {

                // add literals
                {Context.mapOf(), "addedString", "test", FieldType.STRING, "test"},
                {Context.mapOf(), "addedInt", 1, FieldType.INT, 1},
                {Context.mapOf(), "addedBool", true, FieldType.BOOLEAN, true},
                {Context.mapOf(), "addedLong", 5l, FieldType.LONG, 5l},
                {Context.mapOf(), "addedFloat", 12.06f, FieldType.FLOAT, 12.06f},
                {Context.mapOf(), "addedDouble", 12.06d, FieldType.DOUBLE, 12.06d},

                // add via templates
                {Context.mapOf("string", "test"), "addedString", "{{string}}", FieldType.STRING, "test"},
                {Context.mapOf("int", 1), "addedInt", "{{int}}", FieldType.INT, 1},
                {Context.mapOf("bool", true), "addedBool", "{{bool}}", FieldType.BOOLEAN, true},
                {Context.mapOf("long", 5l), "addedLong", "{{long}}", FieldType.LONG, 5l},
                {Context.mapOf("float", 12.06f), "addedFloat", "{{float}}", FieldType.FLOAT, 12.06f},
                {Context.mapOf("double", 12.06d), "addedDouble", "{{double}}", FieldType.DOUBLE, 12.06d},
                {Context.mapOf("bool1", true, "bool2", true), "concatenated", "{{bool1}}{{bool2}}", FieldType.STRING, "truetrue"}
        });
    }
    public AddTest(Map<String, Object> contextValues, String dest, Object value, FieldType fieldType, Object expected) {
        this.contextValues = contextValues;
        this.dest = dest;
        this.value = value;
        this.fieldType = fieldType;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        /*
        Context context = new Context(contextValues);
        Add add = new Add(new TemplateLiteral(value), dest, fieldType);
        add.doExecute(context);
        assertThat(context.hasField(dest)).isTrue();
        Object added = context.getField(dest);
        assertThat(added).isEqualTo(expected);
         */
    }
}
