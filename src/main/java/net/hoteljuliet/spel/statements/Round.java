package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Optional;

@Step(tag = "round")
public class Round extends StatementBaseStep implements Serializable {

    private String source;
    private String format;

    @JsonCreator
    public Round(@JsonProperty(value = "source", required = true) String source,
                 @JsonProperty(value = "format", required = true) String format) {
        super();
        this.source = source;
        this.format = format;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            Double value = context.getField(source);
            DecimalFormat df = new DecimalFormat(format);
            df.setRoundingMode(RoundingMode.DOWN);
            context.replaceFieldValue(source, df.format(value));
        }
        else {
            missingField.increment();
        }
        return NEITHER;
    }
}
