package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.FieldType;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Optional;

@Step(tag = "random-number")
public class RandomNumber extends StepStatement implements Serializable {

    private final String out;
    private final FieldType fieldType;
    private SecureRandom secureRandom;

    @JsonCreator
    public RandomNumber(@JsonProperty(value = "out", required = true) String out,
                        @JsonProperty(value = "type", required = true) FieldType fieldType) {
        this.out = out;
        this.fieldType = fieldType;
        secureRandom = new SecureRandom();

        Preconditions.checkArgument(fieldType != FieldType.STRING && fieldType != FieldType.MAP && fieldType != FieldType.LIST, "field type must be a numeric type");
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == secureRandom) secureRandom = new SecureRandom();

        switch(fieldType) {
            case BOOLEAN: {
                context.addField(out, secureRandom.nextBoolean());
                break;
            }
            case INT: {
                context.addField(out, secureRandom.nextInt());
                break;
            }
            case LONG: {
                context.addField(out, secureRandom.nextLong());
                break;
            }
            case FLOAT: {
                context.addField(out, secureRandom.nextFloat());
                break;
            }
            case DOUBLE: {
                context.addField(out, secureRandom.nextDouble());
                break;
            }
        }
        return EMPTY;
    }
}
