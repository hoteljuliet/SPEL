package io.github.hoteljuliet.spel;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public class Pipeline implements Serializable {

    public static final String[] defaultPredicatePackages = {"io.github.hoteljuliet.spel.predicates"};
    public static final String[] defaultStatementPackages = {"io.github.hoteljuliet.spel.statements"};

    // fields from yaml
    public List<Map<String, Object>> config;
    private List<StepBase> stepBases;
    public Boolean stopOnFailure;

    // fields that are serialized/deserialized
    private StopWatch stopWatch;
    private SummaryStatistics runTimeNanos;
    private LimitedCountingMap stackTraces;
    private LongAdder totalFailures;

    public void init() {
        if (null == stopWatch) stopWatch = new StopWatch();
        if (null == runTimeNanos) runTimeNanos = new SummaryStatistics();
        if (null == stackTraces) stackTraces = new LimitedCountingMap();
        if (null == totalFailures) totalFailures = new LongAdder();
    }

    public static Pipeline fromFile(String path) {
        File file = new File(path);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            Pipeline p = Parser.yamlMapper.readValue(fileReader, Pipeline.class);
            p.init();
            return p;

        } catch (IOException ex) {
           return null;
        }
    }

    public static Pipeline fromString(String value) {
        try {
            Pipeline p = Parser.yamlMapper.readValue(value, Pipeline.class);
            p.init();
            return p;
        }
        catch (JsonProcessingException ex) {
            return null;
        }
    }

    public static Pipeline fromMap(Map<String, Object> config) {
        try {
            String value = Parser.yamlMapper.writeValueAsString(config);
            return fromString(value);
        }
        catch (JsonProcessingException ex) {
            return null;
        }
    }

    public static Pipeline fromResource(String path) {
        try {
            Pipeline p = Parser.yamlMapper.readValue(Pipeline.class.getResourceAsStream(path), Pipeline.class);
            p.init();
            return p;
        }
        catch (IOException ex) {
            return null;
        }
    }

    public Integer parse() {
        Parser parser = new Parser(defaultPredicatePackages, defaultStatementPackages);
        stepBases = parser.parse(config);
        return parser.getInstanceCounter().intValue();
    }

    public Integer parse(String[] predicatePackages, String[] statementPackages) {
        String[] allPredicatePackages = ArrayUtils.addAll(defaultPredicatePackages, predicatePackages);
        String[] allStatementPackages = ArrayUtils.addAll(defaultStatementPackages, statementPackages);
        Parser parser = new Parser(allPredicatePackages, allStatementPackages);
        stepBases = parser.parse(config);
        return parser.getInstanceCounter().intValue();
    }

    /**
     *
     * @return
     */
    public String toMermaid() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("start" + "((start))").append("\n");
        stringBuilder.append("style start fill: #9c1717").append("\n");

        //stringBuilder.append("end" + "(end)").append("\n");
        //stringBuilder.append("style end fill: #030303").append("\n");

        StepBase pointer = null;
        for (int i = 0; i < stepBases.size(); i++) {
            if (i == 0) {
                stepBases.get(i).toMermaid(Optional.empty(), Optional.empty(), stringBuilder);
            }
            else {
                stepBases.get(i).toMermaid(Optional.of(pointer), Optional.empty(), stringBuilder);
            }
            pointer = stepBases.get(i);
        }
        stringBuilder.append("start-->" + stepBases.get(0).name).append("\n");
        return stringBuilder.toString();
    }

    /**
     *
     * @return
     */
    public void execute() {
        execute(new Context(this));
    }

    /**
     *
     * @param context
     * @return
     */
    public void execute(Context context) {
        stopWatch.reset();
        stopWatch.start();
        context.pipelineResult.success = true;
        for (StepBase stepBase : stepBases) {
            try {
                stepBase.execute(context);
            }
            catch(Exception ex) {
                context.pipelineResult.success = true;
                totalFailures.increment();
                List<String> stackTrace = ExceptionUtils.getRootCauseStackTraceList(ex);
                String trace = StringUtils.join(stackTrace, ',');
                stackTraces.put(trace);

                if (stopOnFailure) {
                    break;
                }
            }
            finally {
                ;
            }
        }
        stopWatch.stop();
        runTimeNanos.addValue(stopWatch.getNanoTime());
        context.pipelineResult.averageMillis = Math.round(runTimeNanos.getMean() / 1000000);
        context.pipelineResult.totalMillis = Math.round(runTimeNanos.getSum() / 1000000);
    }

    /**
     *
     * @param stepName
     */
    public Optional<StepBase> find(String stepName) {
        List<StepBase> stack = new ArrayList<>();
        find(stepName, stepBases, stack);
        return stack.isEmpty() ? Optional.empty() : Optional.of(stack.get(0));
    }

    /**
     *
     * @param stepName
     * @param stack
     */
    private void find(String stepName, List<StepBase> search, List<StepBase> stack) {
        for (StepBase stepBase : search) {
            if (stack.size() > 0) {
                break;
            }
            find(stepName, stepBase, stack);
            if (stepBase instanceof StepPredicate) {
                StepPredicate stepPredicate = (StepPredicate)stepBase;
                find(stepName, stepPredicate.onTrue, stack);
                find(stepName, stepPredicate.onFalse, stack);
            }
            else if (stepBase instanceof StepPredicateComplex) {
                StepPredicateComplex stepPredicateComplex = (StepPredicateComplex)stepBase;
                find(stepName, stepPredicateComplex.subPredicate, stack);
                find(stepName, stepPredicateComplex.onTrue, stack);
                find(stepName, stepPredicateComplex.onFalse, stack);
            }
            else if (stepBase instanceof StepStatementComplex) {
                StepStatementComplex stepStatementComplex = (StepStatementComplex)stepBase;
                find(stepName, stepStatementComplex.subStatements, stack);
            }
        }
    }

    private void find(String stepName, StepBase step, List<StepBase> stack) {
        if (stepName.equalsIgnoreCase(step.name)) {
            stack.add(step);
        }
    }

    public List<StepBase> getBaseSteps() {
        return stepBases;
    }

    public SummaryStatistics getRunTimeNanos() {
        return runTimeNanos;
    }

    public LimitedCountingMap getStackTraces() {
        return stackTraces;
    }

    public LongAdder getTotalFailures() {
        return totalFailures;
    }
}
