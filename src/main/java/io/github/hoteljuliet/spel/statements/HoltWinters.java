package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Calculate a triple exponential weighted moving average
 * See Also: https://github.com/elastic/elasticsearch/blob/9ef5d301c299521bdec62226a639c7c1c296242a/server/src/main/java/org/elasticsearch/search/aggregations/pipeline/MovingFunctions.java
 */
@Step(tag = "holt-winters")
public class HoltWinters extends StepStatement implements Serializable {
    private final String in;
    private final Integer capacity;
    private final Double alpha;
    private final Double beta;
    private final Double gamma;
    private final Integer period;
    private final Boolean multiplicative;
    private final String out;
    private final FixedFifo<Double> fixedFifo;

    @JsonCreator
    public HoltWinters(@JsonProperty(value = "in", required = true) String in,
                       @JsonProperty(value = "capacity", required = true) Integer capacity,
                       @JsonProperty(value = "alpha", required = true) Double alpha,
                       @JsonProperty(value = "beta", required = true) Double beta,
                       @JsonProperty(value = "gamma", required = true) Double gamma,
                       @JsonProperty(value = "period", required = true) Integer period,
                       @JsonProperty(value = "mult", required = true) Boolean multiplicative,
                       @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.capacity = capacity;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.period = period;
        this.multiplicative = multiplicative;
        this.out = out;
        fixedFifo = new FixedFifo<>(capacity);
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Double value = context.getField(in);
        fixedFifo.add(value);

        Optional<Double> output = holtWinters(fixedFifo.getList(), alpha, beta, gamma, period, multiplicative);
        if (output.isPresent()) {
            context.addField(out, output.get());
        }
        else {
            softFailure();
        }

        return StepBase.EMPTY;
    }

    /**
     * Calculate a triple exponential weighted moving average
     *
     * Alpha controls the smoothing of the data.  Alpha = 1 retains no memory of past values
     * (e.g. a random walk), while alpha = 0 retains infinite memory of past values (e.g.
     * the series mean).  Useful values are somewhere in between.  Defaults to 0.5.
     *
     * Beta is equivalent to alpha, but controls the smoothing of the trend instead of the data.
     * Gamma is equivalent to alpha, but controls the smoothing of the seasonality instead of the data
     *
     * Only finite values are averaged.  NaN or null are ignored.
     * If all values are missing/null/NaN, the return value will be NaN
     * The average is based on the count of non-null, non-NaN values.
     *
     * @param alpha A double between 0-1 inclusive, controls data smoothing
     * @param beta a double between 0-1 inclusive, controls trend smoothing
     * @param gamma a double between 0-1 inclusive, controls seasonality smoothing
     * @param period the expected periodicity of the data
     * @param multiplicative true if multiplicative HW should be used. False for additive
     */
    private Optional<Double> holtWinters(List<Double> values, double alpha, double beta, double gamma, int period, boolean multiplicative) {

        if (values.size() == 0 || values.size() < period * 2) {
            return Optional.empty();
        }

        double padding = multiplicative ? 0.0000000001 : 0.0;

        // Smoothed value
        double s = 0;
        double last_s;

        // Trend value
        double b = 0;
        double last_b = 0;

        // Seasonal value
        double[] seasonal = new double[values.size()];

        int counter = 0;
        double[] vs = new double[values.size()];
        for (double v : values) {
            if (Double.isNaN(v) == false) {
                vs[counter] = v + padding;
                counter += 1;
            }
        }

        if (counter == 0) {
            return Optional.empty();
        }

        // Initial level value is average of first season
        // Calculate the slopes between first and second season for each period
        for (int i = 0; i < period; i++) {
            s += vs[i];
            b += (vs[i + period] - vs[i]) / period;
        }
        s /= period;
        b /= period;
        last_s = s;

        // Calculate first seasonal
        if (Double.compare(s, 0.0) == 0 || Double.compare(s, -0.0) == 0) {
            Arrays.fill(seasonal, 0.0);
        } else {
            for (int i = 0; i < period; i++) {
                seasonal[i] = vs[i] / s;
            }
        }

        for (int i = period; i < vs.length; i++) {
            if (multiplicative) {
                s = alpha * (vs[i] / seasonal[i - period]) + (1.0d - alpha) * (last_s + last_b);
            } else {
                s = alpha * (vs[i] - seasonal[i - period]) + (1.0d - alpha) * (last_s + last_b);
            }

            b = beta * (s - last_s) + (1 - beta) * last_b;

            if (multiplicative) {
                seasonal[i] = gamma * (vs[i] / (last_s + last_b)) + (1 - gamma) * seasonal[i - period];
            } else {
                seasonal[i] = gamma * (vs[i] - (last_s - last_b)) + (1 - gamma) * seasonal[i - period];
            }
            last_s = s;
            last_b = b;
        }

        int idx = values.size() - period;
        if (multiplicative) {
            return Optional.of((s + b) * seasonal[idx]);
        }
        else {
            return Optional.of(s + b + seasonal[idx]);
        }
    }
}
