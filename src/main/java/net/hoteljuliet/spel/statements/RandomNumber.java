package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.FieldType;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;
import org.apache.commons.codec.binary.Hex;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Optional;

@Step(tag = "rng")
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
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (null == secureRandom) secureRandom = new SecureRandom();

        switch(fieldType) {
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
            case STRING: {
                context.addField(dest, String.valueOf(secureRandom.nextInt()));
            }
        }
        return EMPTY;
    }
}
