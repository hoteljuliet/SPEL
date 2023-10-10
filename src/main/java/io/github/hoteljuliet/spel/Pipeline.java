package io.github.hoteljuliet.spel;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.hoteljuliet.spel.metrics.MetricsProvider;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class Pipeline implements Serializable {

    public static final String[] emptyPackage = {};
    public static final String[] defaultPredicatePackages = {"io.github.hoteljuliet.spel.predicates"};
    public static final String[] defaultStatementPackages = {"io.github.hoteljuliet.spel.statements"};

    // fields from yaml
    public String name;
    public List<Map<String, Object>> config;
    public List<StepBase> stepBases;
    public Boolean stopOnFailure;

    private StopWatch stopWatch;
    private AtomicLong totalNs;
    private AtomicLong maxNs;
    private AtomicLong avgNs;
    private AtomicLong invocations;
    private AtomicLong success;
    private AtomicLong exception;

    public void init() {
        if (null == stopWatch) stopWatch = new StopWatch();
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

    public Integer parse(MetricsProvider metricsProvider) {
        return this.parse(metricsProvider, emptyPackage, emptyPackage);
    }

    public Integer parse(MetricsProvider metricsProvider, String[] predicatePackages, String[] statementPackages) {
        String[] allPredicatePackages = ArrayUtils.addAll(defaultPredicatePackages, predicatePackages);
        String[] allStatementPackages = ArrayUtils.addAll(defaultStatementPackages, statementPackages);
        Parser parser = new Parser(metricsProvider, allPredicatePackages, allStatementPackages);
        stepBases = parser.parse(config);

        exception = metricsProvider.provideNext(name, MetricsProvider.EXCEPTION);
        invocations = metricsProvider.provideNext(name, MetricsProvider.INVOCATIONS);
        success = metricsProvider.provideNext(name, MetricsProvider.SUCCESS);
        totalNs = metricsProvider.provideNext(name, MetricsProvider.TOTAL_NS);
        maxNs = metricsProvider.provideNext(name, MetricsProvider.MAX_NS);
        avgNs = metricsProvider.provideNext(name, MetricsProvider.AVG_NS);

        return parser.getInstanceCounter().intValue();
    }

    public String toMermaid() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("start" + "((start))").append("\n");
        stringBuilder.append("style start fill: #9c1717").append("\n");

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

    public void execute() {
        execute(new Context(this));
    }

    public void execute(Context context) {

        try {
            // pipeline stopwatch and metrics
            stopWatch.reset();
            stopWatch.start();
            invocations.getAndIncrement();

            // run the steps
            for (StepBase stepBase : stepBases) {
                try {
                    stepBase.execute(context);
                } catch (Exception ex) {
                    if (stopOnFailure) {
                        break;
                    }
                } finally {
                    ; // finally after each step
                }
            }
            success.getAndIncrement();
        }
        catch(Exception ex) {
            exception.getAndIncrement();
        }
        finally {
            // pipeline stopwatch and metrics
            stopWatch.stop();
            Long currentNs = stopWatch.getNanoTime();
            maxNs.set(Math.max(currentNs, maxNs.get()));
            totalNs.getAndAdd(currentNs);
            avgNs.set(totalNs.get() / invocations.get());
        }
    }

    public Optional<StepBase> find(String stepName) {
        List<StepBase> stack = new ArrayList<>();
        find(stepName, stepBases, stack);
        return stack.isEmpty() ? Optional.empty() : Optional.of(stack.get(0));
    }

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
}
