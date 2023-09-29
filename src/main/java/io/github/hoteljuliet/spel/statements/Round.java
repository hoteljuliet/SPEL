package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Optional;

@Step(tag = "round")
public class Round extends StepStatement implements Serializable {

    private String in;
    private String format;

    /**
     *
     * @param in the path to a Double in the Context
     * @param format the format to reformat the double with, See {@link java.text.DecimalFormat}
     */
    @JsonCreator
    public Round(@JsonProperty(value = "in", required = true) String in,
                 @JsonProperty(value = "format", required = true) String format) {
        super();
        this.in = in;
        this.format = format;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Double v = context.getField(in);
        DecimalFormat df = new DecimalFormat(format);
        df.setRoundingMode(RoundingMode.DOWN);
        context.replaceFieldValue(in, df.format(v));
        return EMPTY;
    }
}
