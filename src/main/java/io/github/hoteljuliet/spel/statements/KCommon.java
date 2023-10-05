package io.github.hoteljuliet.spel.statements;

import com.clearspring.analytics.stream.frequency.ConservativeAddSketch;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 *
 */
@Step(tag = "k-common")
public class KCommon extends StepStatement implements Serializable {

    private final String in;
    private final String out;
    private final Integer k;
    private final Double confidence;
    private final Double eps;
    private final Map<Object, Long> common;
    private ConservativeAddSketch conservativeAddSketch;

    /**
     * @param in a path in the context to a String to find the k most common values of
     * @param eps the eps - TODO: document
     * @param confidence confidence, max value is 1.00
     * @param k the number of common values to track
     * @param out a path into the context to place the map of common values
     */
    @JsonCreator
    public KCommon(@JsonProperty(value = "in", required = true) String in,
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
            common.clear();
        }
    }
}
