package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepStatement;
import com.github.hoteljuliet.spel.StepBase;
import com.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Step(tag = "regression")
public class Regression extends StepStatement implements Serializable {
    private final String x;
    private final String y;
    private final TemplateLiteral predict;
    private final String dest;
    private final SimpleRegression regression;

    @JsonCreator
    public Regression(@JsonProperty(value = "x", required = true) String x,
                      @JsonProperty(value = "y", required = true) String y,
                      @JsonProperty(value = "predict", required = true) TemplateLiteral predict,
                      @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.x = x;
        this.y = y;
        this.predict = predict;
        this.dest = dest;
        this.regression = new SimpleRegression();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Double valueX = context.getField(x);
        Double valueY = context.getField(y);
        regression.addData(valueX, valueY);

        if (regression.getN() >= 3) {
            Map<String, Double> output = new HashMap<>();
            output.put("intercept", regression.getIntercept());
            output.put("slope", regression.getSlope());
            output.put("slopeStdErr", regression.getSlopeStdErr());
            Double predictX = predict.get(context);
            output.put("predictedY", regression.predict(predictX));
            context.addField(dest, output);
        }
        return StepBase.EMPTY;
    }

    @Override
    public void clear() {
        regression.clear();
    }
}
