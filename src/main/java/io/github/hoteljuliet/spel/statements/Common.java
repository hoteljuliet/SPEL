package io.github.hoteljuliet.spel.statements;

import com.clearspring.analytics.stream.frequency.ConservativeAddSketch;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;
import org.apache.commons.lang3.RandomUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
  *        int numItems = 10000000;
 *         int maxScale = 15000;
 *         double epsOfTotalCount = 0.00075;
 *         double errorRange = epsOfTotalCount;
 *         double confidence = 0.99;
 *
 *         int[] actualFreq = new int[maxScale];
 *         IFrequency sketch = new ConservativeAddSketch(epsOfTotalCount, confidence, seed);
 *         IFrequency baseSketch = new CountMinSketch(epsOfTotalCount, confidence, seed);
 */
@Step(tag = "common")
public class Common extends StepStatement implements Serializable {

    private final String in;
    private final String out;
    private final Integer k;
    private final Double confidence;
    private final Double eps;
    private final Map<Object, Long> common;
    private ConservativeAddSketch conservativeAddSketch;

    @JsonCreator
    public Common(@JsonProperty(value = "in", required = true) String in,
                  @JsonProperty(value = "eps", required = true) Double eps,
                  @JsonProperty(value = "confidence", required = true) Double confidence,
                  @JsonProperty(value = "k", required = true) Integer k,
                  @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.k = k;
        this.confidence = confidence;
        this.eps = eps;
        this.out = out;
        common = new HashMap<>();
        Random random = new Random();
        conservativeAddSketch = new ConservativeAddSketch(this.eps, this.confidence, random.nextInt());
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        synchronized (this) {
            Object value = context.getField(in);
            conservativeAddSketch.add(value.toString(), 1);
            Long count = conservativeAddSketch.estimateCount(value.toString());

            // if the key already exists, update the new count
            if (common.containsKey(value)) {
                common.put(value, count);
            }
            // if common is not yet full, just add
            else if (common.size() < k) {
                common.put(value, count);
            }
            // else, evict an existing common member if more common value found
            else {
                for (Map.Entry<Object, Long> entry : common.entrySet()) {
                    if (entry.getValue() < count) {
                        common.remove(entry.getKey());
                        common.put(value, count);
                        break;
                    }
                }
            }

            context.addField(out, common);
            return EMPTY;
        }
    }

    @Override
    public void clear() {
        synchronized (this) {
            if (conservativeAddSketch.getConfidence() < confidence || conservativeAddSketch.getRelativeError() > eps) {
                Random random = new Random();
                conservativeAddSketch = new ConservativeAddSketch(this.eps, this.confidence, random.nextInt());
            }
        }
    }
}
