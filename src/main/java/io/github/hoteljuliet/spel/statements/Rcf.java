package io.github.hoteljuliet.spel.statements;

import com.amazon.randomcutforest.RandomCutForest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * TODO: implement anomaly detect ion via RCF - must be serializable
 * See Also: https://github.com/aws/random-cut-forest-by-aws
 */
@Step(tag = "rcf")
public class Rcf extends StepStatement implements Serializable {
    private final String out;
    private final Integer dimensions;
    private final Integer trees;
    private final Integer samples;
    private final String precision;
    private String json;
    private List<Double> point;
    private transient RandomCutForest randomCutForest;

    @JsonCreator
    public Rcf(@JsonProperty(value = "out", required = true) String out,
               @JsonProperty(value = "dimensions", required = true) Integer dimensions,
               @JsonProperty(value = "trees", required = true) Integer trees,
               @JsonProperty(value = "samples", required = true) Integer samples,
               @JsonProperty(value = "precision", required = true) String precision) throws JsonProcessingException {
        super();
        this.out = out;
        this.dimensions = dimensions;
        this.trees = trees;
        this.samples = samples;
        this.precision = precision;

        /*
        randomCutForest = RandomCutForest.builder().compact(true)
                .dimensions(dimensions)
                .numberOfTrees(trees)
                .sampleSize(samples)
                .precision(Precision.valueOf(precision))
                .build();

        RandomCutForestMapper mapper = new RandomCutForestMapper();
        mapper.setSaveExecutorContextEnabled(true);
        json = Parser.jsonMapper.writeValueAsString(mapper.toState(randomCutForest));
        point = new ArrayList<>();
         */
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        /*
        if (null == point) point = new ArrayList<>();
        if (null == randomCutForest && null != json) {
            RandomCutForestMapper mapper = new RandomCutForestMapper();
            randomCutForest = mapper.toModel(Parser.jsonMapper.readValue(json, RandomCutForestState.class));
        }

        if (point.size() == )
         */

        //randomCutForest.update();
        return EMPTY;
    }
}