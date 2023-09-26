package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.FieldType;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepStatement;
import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Optional;

@Step(tag = "random-number")
public class RandomNumber extends StepStatement implements Serializable {

    private final String dest;
    private final FieldType fieldType;
    private SecureRandom secureRandom;

    @JsonCreator
    public RandomNumber(@JsonProperty(value = "dest", required = true) String dest,
                        @JsonProperty(value = "type", required = true) FieldType fieldType) {
        this.dest = dest;
        this.fieldType = fieldType;
        secureRandom = new SecureRandom();

        Preconditions.checkArgument(fieldType != FieldType.STRING && fieldType != FieldType.MAP && fieldType != FieldType.LIST, "field type must be a numeric type");
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == secureRandom) secureRandom = new SecureRandom();

        switch(fieldType) {
            case BOOLEAN: {
                context.addField(dest, secureRandom.nextBoolean());
                break;
            }
            case INT: {
                context.addField(dest, secureRandom.nextInt());
                break;
            }
            case LONG: {
                context.addField(dest, secureRandom.nextLong());
                break;
            }
            case FLOAT: {
                context.addField(dest, secureRandom.nextFloat());
                break;
            }
            case DOUBLE: {
                context.addField(dest, secureRandom.nextDouble());
                break;
            }
        }
        return EMPTY;
    }
}
