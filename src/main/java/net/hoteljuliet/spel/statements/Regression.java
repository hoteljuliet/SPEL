package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.io.Serializable;
import java.util.*;

@Step(tag = "regression")
public class Regression extends StepStatement implements Serializable {
    private final String x;
    private final String y;
    private final String predict;
    private final String dest;
    private final SimpleRegression regression;

    @JsonCreator
    public Regression(@JsonProperty(value = "x", required = true) String x,
                      @JsonProperty(value = "y", required = true) String y,
                      @JsonProperty(value = "predict", required = true) String predict,
                      @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.x = x;
        this.y = y;
        this.predict = predict;
        this.dest = dest;
        regression = new SimpleRegression();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasFields(x, y, predict)) {

            Double valueX = context.getField(x);
            Double valueY = context.getField(y);
            regression.addData(valueX, valueY);

            if (regression.getN() >= 3) {
                Map<String, Double> output = new HashMap<>();
                output.put("intercept", regression.getIntercept());
                output.put("slope", regression.getSlope());
                output.put("slopeStdErr", regression.getSlopeStdErr());
                Double predictX = context.getField(predict);
                output.put("predictedY", regression.predict(predictX));
                context.addField(dest, output);
            }
        }
        else {
            missingField.increment();
        }
        return StepBase.NEITHER;
    }
}
