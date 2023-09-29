package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Action;
import io.github.hoteljuliet.spel.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(Parameterized.class)
public class NowTest {

    private final String out;
    private final String format;
    private final String zone;
    private final Object expected;

    public NowTest(String out, String format, String zone, Object expected) {
        this.out = out;
        this.format = format;
        this.zone = zone;
        this.expected = expected;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "now", "MM-dd-YY", "UTC", DateTimeFormatter.ofPattern("MM-dd-YY").format(ZonedDateTime.now(ZoneId.of("UTC")))},
        });
    }

    @Test
    public void test() throws Exception {
        Now now = new Now(out, format, zone);
        Context context = new Context();
        now.doExecute(context);
        assertThat(context.hasField(out)).isTrue();
        String actual = context.getField(out);
        assertThat(actual).isEqualTo(expected);
    }
}
