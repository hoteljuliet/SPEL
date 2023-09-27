package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * Injects a
 */
@Step(tag = "ramp")
public class Ramp extends StepStatement implements Serializable {
    private final String dest;
    private final Integer peak;
    private Integer ramp;
    private Boolean rampingUp;

    private final String suffix;

    @JsonCreator
    public Ramp(@JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "peak", required = true) Integer peak) {
        super();
        this.dest = dest;
        this.peak = peak;
        this.ramp = 0;
        suffix = "_" + RandomStringUtils.randomAlphabetic(8);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (ramp >= peak) rampingUp = false;
        else if (ramp <= 0) rampingUp = true;

        if (rampingUp) {
            ramp += 1;
        }
        else {
            ramp -= 1;
        }
        context.addField(dest + suffix, ramp);
        return EMPTY;
    }
}
