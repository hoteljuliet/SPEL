package io.github.hoteljuliet.spel.statements;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.FieldType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class RandomStringTest {
    private final String out;
    private final Integer length;

    public RandomStringTest(String out, Integer length) {
        this.out = out;
        this.length = length;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "field", 1},
            { "field", 8},
            { "field", 16},
            { "field", 32}
        });
    }

    @Test
    public void test() throws Exception {
        RandomString randomString = new RandomString(out, length);
        Context context = new Context();
        randomString.doExecute(context);
        String actual = context.getField(out);
        assertThat(actual.length()).isEqualTo(length);
    }
}
