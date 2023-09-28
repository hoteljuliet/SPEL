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

    private final String dest;
    private final String format;
    private final String zone;
    private final Object expected;

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "now", "MM-dd-YY", "UTC", DateTimeFormatter.ofPattern("MM-dd-YY").format(ZonedDateTime.now(ZoneId.of("UTC")))},
        });
    }

    public NowTest(String dest, String format, String zone, Object expected) {
        this.dest = dest;
        this.format = format;
        this.zone = zone;
        this.expected = expected;
    }

    @Test
    public void test() throws Exception {
        Now now = new Now(dest, format, zone);
        Context context = new Context();
        now.doExecute(context);
        assertThat(context.hasField(dest)).isTrue();
        String actual = context.getField(dest);
        assertThat(actual).isEqualTo(expected);
    }
}
