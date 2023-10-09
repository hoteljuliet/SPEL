package io.github.hoteljuliet.spel;

import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

public class DateFormats {

    public static DateTimeFormatter of(String pattern) {

        switch (pattern.toUpperCase()) {
            case "ELASTIC": {
                return ELASTIC;
            }
            case "ISO8601": {
                return ISO8601;
            }
            case "UNIX_S": {
                return UNIX_S;
            }
            case "UNIX_MS": {
                return UNIX_MS;
            }
        }

        return DateTimeFormatter.ofPattern(pattern);
    }

    public static final DateTimeFormatter ELASTIC = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(ISO_LOCAL_DATE)
            .appendLiteral('T')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .optionalStart()
            .appendFraction(NANO_OF_SECOND, 3, 3, true)
            .optionalStart()
            .appendOffsetId()
            .optionalEnd()
            .toFormatter()
            .withChronology(IsoChronology.INSTANCE)
            .withResolverStyle(ResolverStyle.STRICT);

    public static final DateTimeFormatter ISO8601 = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(ISO_LOCAL_DATE)
            .appendLiteral('T')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .optionalStart()
            .optionalStart()
            .appendLiteral(',')
            .optionalEnd()
            .optionalStart()
            .appendLiteral('.')
            .optionalEnd()
            .appendFraction(NANO_OF_SECOND, 0, 9, false)
            .optionalStart()
            .appendOffsetId()
            .optionalEnd()
            .optionalEnd()
            .optionalEnd()
            .toFormatter()
            .withChronology(IsoChronology.INSTANCE)
            .withResolverStyle(ResolverStyle.STRICT);

    public static final DateTimeFormatter UNIX_S = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.INSTANT_SECONDS, 1, 10, SignStyle.NEVER)
            .toFormatter();

    public static final DateTimeFormatter UNIX_MS = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.INSTANT_SECONDS, 1, 10, SignStyle.NEVER)
            .appendValue(ChronoField.MILLI_OF_SECOND, 3)
            .toFormatter();
}
