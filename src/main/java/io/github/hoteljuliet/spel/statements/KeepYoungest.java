package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Maintain a Map of
 */
@Step(tag = "keep-youngest")
public class KeepYoungest extends StepStatement implements Serializable {
    private final String in;
    private final String eventTime;
    private final String now;
    private final Long max;
    private final String out;
    private final TreeMap<Long, Map<String, Object>> values;

    @JsonCreator
    public KeepYoungest(@JsonProperty(value = "in", required = true) String in,
                        @JsonProperty(value = "event-time", required = true) String eventTime,
                        @JsonProperty(value = "now", required = true) String now,
                        @JsonProperty(value = "max", required = true) Long max,
                        @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.eventTime = eventTime;
        this.now = now;
        this.max = max;
        this.out = out;
        values = new TreeMap<>();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Map<String, Object> value = context.getField(in);
        Long eventEpoch = context.getField(eventTime);
        Long nowEpoch = context.getField(now);
        Long cutoff = nowEpoch - max;

        // if age of event is greater than the cutoff, add it to values
        if (eventEpoch >= cutoff) {
            values.put(eventEpoch, value);
        }

        Map.Entry<Long, Map<String, Object>> evicted = values.floorEntry(cutoff);
        while(null != evicted) {
            values.remove(evicted.getKey());
            evicted = values.floorEntry(cutoff);
        }

        List<Map<String, Object>> valuesList = new ArrayList<>(values.values());
        context.addField(out, valuesList);
        return EMPTY;
    }
}

