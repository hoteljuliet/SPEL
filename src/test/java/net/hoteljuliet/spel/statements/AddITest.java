package net.hoteljuliet.spel.statements;

import net.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AddITest {
    private final String dest;
    private final Object value;
    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"string", "0"},
                {"int", 1},
                {"bool", true},
                {"long", 5l},
                {"float", 12.06f},
                {"double", 12.06d},
                {"map", new HashMap<>()},
                {"list", new ArrayList<>()}
        });
    }

    public AddITest(String dest, Object value) {
        this.dest = dest;
        this.value = value;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context();
        AddI addI = new AddI(dest, value);
        addI.doExecute(context);
        assertThat(context.hasField(dest)).isTrue();
        Object added = context.getField(dest);
        assertThat(added).isEqualTo(value);
    }
}
