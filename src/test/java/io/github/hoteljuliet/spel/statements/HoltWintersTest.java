package io.github.hoteljuliet.spel.statements;

import com.google.common.collect.EvictingQueue;
import io.github.hoteljuliet.spel.Context;
import org.assertj.core.data.Percentage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HoltWintersTest {
    private final String in;
    private final Integer capacity;
    private final Double alpha;
    private final Double beta;
    private final Double gamma;
    private final Integer period;
    private final Boolean multiplicative;
    private final String out;

    public HoltWintersTest(String in, Integer capacity, Double alpha, Double beta, Double gamma, Integer period,
                           Boolean multiplicative, String out) {
        this.in = in;
        this.capacity = capacity;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.period = period;
        this.multiplicative = multiplicative;
        this.out = out;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "field", 32, 1.0, 1.0, 1.0, 1, false, "smooth"}
        });
    }

    @Test
    public void test() throws Exception {
        Random random = new Random();
        HoltWinters holtWinters = new HoltWinters(in, capacity, alpha, beta, gamma, period, multiplicative, out);
        EvictingQueue<Double> window = EvictingQueue.create(capacity);
        Context context = new Context();

        for (int i = 0; i < (capacity * 2); i++) {
            Double randomDouble = random.nextDouble();
            context.addField(in, randomDouble);
            holtWinters.doExecute(context);
            window.offer(randomDouble);
        }

        // Smoothed value
        double s = 0;
        double last_s = 0;

        // Trend value
        double b = 0;
        double last_b = 0;

        // Seasonal value
        double[] seasonal = new double[capacity];

        int counter = 0;
        double[] vs = new double[capacity];
        for (double v : window) {
            vs[counter] = v;
            counter += 1;
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
            s = alpha * (vs[i] - seasonal[i - period]) + (1.0d - alpha) * (last_s + last_b);
            b = beta * (s - last_s) + (1 - beta) * last_b;

            seasonal[i] = gamma * (vs[i] - (last_s - last_b)) + (1 - gamma) * seasonal[i - period];
            last_s = s;
            last_b = b;
        }

        int idx = window.size() - period + (0 % period);
        double expected = s + (1 * b) + seasonal[idx];
        double actual = context.getField(out);
        assertThat(actual).isCloseTo(expected, Percentage.withPercentage(0.01));
    }
}
