package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.BaseStep;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * TODO: implement anomaly detect ion via RCF - must be serializable + restoreable!
 */
@Step(tag = "anomaly-rcf")
public class AnomalyDetectRCF extends StatementBaseStep implements Serializable {
    private final String source;
    private final String dest;
    private final String delimiter;

    @JsonCreator
    public AnomalyDetectRCF(@JsonProperty(value = "source", required = true) String source,
                            @JsonProperty(value = "delimiter", required = true) String delimiter,
                            @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.delimiter = delimiter;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            // TODO: implement
            // see https://github.com/aws/random-cut-forest-by-aws/blob/main/Java/examples/src/main/java/com/amazon/randomcutforest/examples/parkservices/SequentialAnomalyExample.java
        }
        else {
            missingField.increment();
        }
        return BaseStep.NEITHER;
    }
}