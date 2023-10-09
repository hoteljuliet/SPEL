package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.FieldType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class RandomNumberTest {
    private final String out;
    private final FieldType fieldType;

    public RandomNumberTest(String out, FieldType fieldType) {
        this.out = out;
        this.fieldType = fieldType;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "field", FieldType.INT},
            { "field", FieldType.LONG},
            { "field", FieldType.FLOAT},
            { "field", FieldType.DOUBLE}
        });
    }

    @Test
    public void test() throws Exception {
        RandomNumber randomNumber = new RandomNumber(out, fieldType);
        Context context = new Context();
        randomNumber.doExecute(context);
        Number actual = context.getField(out);
        assertThat(fieldType.isInstance(actual)).isTrue();
    }
}
