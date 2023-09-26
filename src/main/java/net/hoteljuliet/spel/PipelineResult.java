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
        success = false;
        totalMillis = 0l;
        averageMillis = 0l;
        predicateResults = new HashMap<>();
        statementResults = new HashMap<>();
    }

    public PipelineResult(PipelineResult other) {
        this();
        this.success = other.success;
        this.totalMillis = other.totalMillis;
        this.averageMillis = other.averageMillis;
        this.predicateResults.putAll(other.predicateResults);
        this.statementResults.putAll(other.statementResults);
    }

    public Long getTotalSoftFailures() {
        Long retVal = 0l;
        for (PredicateResult p : predicateResults.values()) {
            retVal += p.softFailure;
        }
        for (StatementResult s : statementResults.values()) {
            retVal += s.softFailure;
        }
        return retVal;
    }
}
