package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "random-string")
public class RandomString extends StepStatement implements Serializable {
    private final String dest;
    private final Integer length;

    @JsonCreator
    public RandomString(@JsonProperty(value = "dest", required = true) String dest,
                        @JsonProperty(value = "length", required = true) Integer length) {
        this.dest = dest;
        this.length = length;

    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String randomString = RandomStringUtils.randomAlphanumeric(length);
        context.addField(dest, randomString);
        return EMPTY;
    }
}
