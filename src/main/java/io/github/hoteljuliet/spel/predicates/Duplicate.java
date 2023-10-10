package io.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepPredicate;

import java.util.Optional;

/**
 * Returns true iff the event is a duplicate of another event
 */
@Step(tag = "duplicate")
public class Duplicate extends StepPredicate {

    private final String in;
    private final Integer insertions;
    private final Double fpp;
    private BloomFilter<byte[]> bloomFilter;

    public Duplicate(@JsonProperty(value = "in", required = true) String in,
                     @JsonProperty(value = "insertions", required = true) Integer insertions,
                     @JsonProperty(value = "fpp", required = true) Double fpp) {
        super();
        this.in = in;
        this.insertions = insertions;
        this.fpp = fpp;
        bloomFilter = BloomFilter.create(Funnels.byteArrayFunnel(), insertions, fpp);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        synchronized (this) {
            if (context.hasField(in)) {
                String value = context.getField(in);
                if (bloomFilter.mightContain(value.getBytes())) {
                    return TRUE;
                } else {
                    return FALSE;
                }
            } else {
                softFailure();
                return FALSE;
            }
        }
    }

    @Override
    public void clear() {
        synchronized (this) {
            if (bloomFilter.expectedFpp() > fpp) {
                bloomFilter = BloomFilter.create(Funnels.byteArrayFunnel(), insertions, fpp);
            }
        }
    }
}
