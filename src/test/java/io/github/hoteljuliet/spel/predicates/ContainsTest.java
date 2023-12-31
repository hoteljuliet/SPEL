package io.github.hoteljuliet.spel.predicates;

import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ContainsTest {
    private final Map<String, Object> contextValues;
    private final TemplateLiteral value;
    private final TemplateLiteral list;
    private final Boolean expected;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {

                // literal in template
                {Context.mapOf("list", Arrays.asList(1, 2, 3)), 1, "{{list}}", true},

                // template in literal
                {Context.mapOf("value", 1), "{{value}}", Arrays.asList(1, 2, 3), true},

                // template in template
               {Context.mapOf("value", 1, "list", Arrays.asList(1, 2, 3)), "{{value}}", "{{list}}", true}
        });
    }
    public ContainsTest(Map<String, Object> contextValues, Object value, Object list, Boolean expected) {
        this.contextValues = contextValues;
        this.list = new TemplateLiteral(list);
        this.value = new TemplateLiteral(value);
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Context context = new Context(contextValues);
        Contains contains = new Contains(value, list);
        Optional<Boolean> result = contains.doExecute(context);
        assertThat(result.isPresent());
        assertThat(result.get()).isEqualTo(expected);
    }
}
