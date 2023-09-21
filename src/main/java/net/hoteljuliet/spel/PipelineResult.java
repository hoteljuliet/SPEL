package net.hoteljuliet.spel;

import java.util.HashMap;
import java.util.Map;

public class PipelineResult {

    public Boolean success;
    public Long totalMillis;
    public Long averageMillis;

    public Map<String, PredicateResult> predicateResults;
    public Map<String, StatementResult> statementResults;

    public PipelineResult() {
        predicateResults = new HashMap<>();
        statementResults = new HashMap<>();
    }
}
