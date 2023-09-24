package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Optional;

@Step(tag = "round")
public class Round extends StepStatement implements Serializable {

    private String value;
    private String format;

    @JsonCreator
    public Round(@JsonProperty(value = "value", required = true) String value,
                 @JsonProperty(value = "format", required = true) String format) {
        super();
        this.value = value;
        this.format = format;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Double v = context.getField(value);
        DecimalFormat df = new DecimalFormat(format);
        df.setRoundingMode(RoundingMode.DOWN);
        context.replaceFieldValue(value, df.format(v));
        return EMPTY;
    }
}
