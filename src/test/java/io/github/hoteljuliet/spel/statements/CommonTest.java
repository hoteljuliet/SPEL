package io.github.hoteljuliet.spel.statements;


import io.github.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonTest {

    @Test
    public void test() throws Exception {
        Common common = new Common("field", 0.0005, 0.99, 5, "common");
        Context context = new Context();
        Map<String, Object> values = Context.mapOf("a", 100, "b", 200, "c", 400, "d", 800, "e", 1200, "f", 1600);

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            for (int i = 0; i < (int)entry.getValue(); i++) {
                context.addField("field", entry.getKey());
                common.doExecute(context);
            }
        }

        Map<String, Object> commonValues = context.getField("common");
        assertThat(commonValues.size()).isEqualTo(5);
        assertThat(commonValues.containsKey("a")).isFalse();
        assertThat(commonValues.get("b")).isEqualTo(200l);
        assertThat(commonValues.get("c")).isEqualTo(400l);
        assertThat(commonValues.get("d")).isEqualTo(800l);
        assertThat(commonValues.get("e")).isEqualTo(1200l);
        assertThat(commonValues.get("f")).isEqualTo(1600l);
    }
}
