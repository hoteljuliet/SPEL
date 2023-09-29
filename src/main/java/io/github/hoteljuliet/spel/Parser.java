package io.github.hoteljuliet.spel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.hoteljuliet.spel.metrics.DefaultMetricsProvider;
import io.github.hoteljuliet.spel.metrics.MetricsProvider;
import io.github.hoteljuliet.spel.predicates.If;
import org.apache.commons.text.StringSubstitutor;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class Parser {
    public static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    public static final ObjectMapper jsonMapper = new ObjectMapper();
    private final LongAdder instanceCounter;
    private final Map<String, Class> predicateTypesMap;
    private final Map<String, Class> statementTypesMap;
    private final Set<String> userProvidedNames;
    private final MetricsProvider metricsProvider;

    public Parser() {
        this(new DefaultMetricsProvider(), Pipeline.defaultPredicatePackages, Pipeline.defaultStatementPackages);
    }

    public Parser(String[] predicatePackages, String[] statementPackages) {
        this(new DefaultMetricsProvider(), predicatePackages, statementPackages);
    }

    public Parser(MetricsProvider metricsProvider, String[] predicatePackages, String[] statementPackages) {
        this.metricsProvider = metricsProvider;
        userProvidedNames = new HashSet<>();
        instanceCounter = new LongAdder();
        predicateTypesMap = new HashMap<>();
        statementTypesMap = new HashMap<>();
        populateTypesMap(predicatePackages, predicateTypesMap);
        populateTypesMap(statementPackages, statementTypesMap);
    }

    public List<StepBase> parse(List<Map<String, Object>> config) {
        if (config == null) {
            throw new RuntimeException("empty config");
        }

        for (int i = 0; i < config.size(); i++) {
            Map<String, Object> node = config.get(i);
            if (null == node) {
                throw new RuntimeException("config entries cannot be null, see entry #" + (i+1));
            }
        }
        return config.stream().map(this::parse).collect(Collectors.toList());
    }

    public StepBase parse(Map<String, Object> node) {
        String firstKey = firstKey(node);

        if (firstKey.contains("if-")) {
            node.remove(firstKey);
            String[] parts = firstKey.split("-");
            String name = parts[1];
            return parsePredicate(Optional.of(name), node);
        }
        else if (firstKey.equalsIgnoreCase("if")) {
            node.remove(firstKey);
            return parsePredicate(Optional.empty(), node);
        }
        else {
            return parseStatement(node);
        }
    }

    public StepBase parsePredicate(Optional<String> name, Map<String, Object> node) {

        Object firstValue = firstValue(node);
        String type = firstKey(node);

        List<Map<String, Object>> onTrue = (List<Map<String, Object>>) node.get("then");

        List<Map<String, Object>> onFalse = new ArrayList<>();
        if (node.containsKey("else")) {
            onFalse = (List<Map<String, Object>>) node.get("else");
        }

        If ifPredicate = new If();

        StepBase predicate = null;
        // this is an "and" or "or" type predicate (one with multiple sub-predicates)
        if (firstValue instanceof List) {
            predicate = buildComplexPredicate(type, (List<Map<String, Object>>) node.get(type));
        }
        // this is a simple/single predicate
        else {
            predicate = buildPredicate(type, (Map<String, Object>) node.get(type));
        }
        ifPredicate.predicate = predicate;

        for (Map<String, Object> map : onTrue) {
            StepBase s = parse(map);
            ifPredicate.onTrue.add(s);
        }

        for (Map<String, Object> map : onFalse) {
            StepBase s = parse(map);
            ifPredicate.onFalse.add(s);
        }

        if (name.isPresent()) {
            ifPredicate.setName(buildUniqueNameFromName(name.get()));
        }
        else {
            ifPredicate.setName(buildUniqueNameFromType("if"));
        }

        setMetricsFromProvider(ifPredicate);

        return ifPredicate;
    }

    public StepBase parseStatement(Map<String, Object> node) {
        String type = firstKey(node);

        if (type.startsWith("foreach") || type.startsWith("sort")) {
            String[] parts = type.split("-");
            String typeName = parts[0];
            String source = parts[1];
            return buildComplexStatement(typeName, source, (List<Map<String, Object>>) node.get(type));
        }
        else {
            return buildStatement(type, (Map<String, Object>) node.get(type));
        }
    }

    public static String firstKey(Map<String, Object> node) {
        return node.entrySet().iterator().next().getKey();
    }

    public static Object firstValue(Map<String, Object> node) {
        return node.entrySet().iterator().next().getValue();
    }

    private void populateTypesMap(String[] packages, Map<String, Class> typesMap) {
        for (String packageName : packages) {
            Reflections reflections = new Reflections(packageName);
            Set<Class<?>> stepTypes = reflections.getTypesAnnotatedWith(Step.class);
            for (Class clazz : stepTypes) {
                Step stepAnnotation = (Step) clazz.getAnnotation(Step.class);
                if (typesMap.containsKey(stepAnnotation.tag())) {
                    throw new RuntimeException(" - Step tags must be unique, but a duplicate step tag has been found. " +
                            "Check @Step annotations for a duplicate: " + stepAnnotation.tag());
                }
                else {
                    typesMap.put(stepAnnotation.tag(), clazz);
                }
            }
        }
    }

    public StepBase buildStatement(String type, Map<String, Object> config) {
        try {
            replaceConfigWithEnvVars(config);
            buildUniqueNameFromType(type, config);
            return buildBaseStep(type, config, statementTypesMap);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public StepBase buildPredicate(String type, Map<String, Object> config) {
        try {
            replaceConfigWithEnvVars(config);
            buildUniqueNameFromType(type, config);
            return buildBaseStep(type, config, predicateTypesMap);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public StepBase buildBaseStep(String type, Map<String, Object> config, Map<String, Class> typesMap) throws Exception {
        if (typesMap.containsKey(type)) {
            Class clazz = typesMap.get(type);
            StepBase stepBase = (StepBase) yamlMapper.readValue(yamlMapper.writeValueAsBytes(config), clazz);
            setMetricsFromProvider(stepBase);
            return stepBase;
        }
        else {
            throw new RuntimeException("no type found for: " + type);
        }
    }

    private void setMetricsFromProvider(StepBase stepBase) {
        // set the metrics in the steps from an external provider
        stepBase.invocations = metricsProvider.provideNext(stepBase.name, MetricsProvider.INVOCATIONS);
        stepBase.success = metricsProvider.provideNext(stepBase.name, MetricsProvider.SUCCESS);
        stepBase.softFailure = metricsProvider.provideNext(stepBase.name, MetricsProvider.SOFT_FAIL);
        stepBase.exception = metricsProvider.provideNext(stepBase.name, MetricsProvider.EXCEPTION);
        stepBase.totalNs = metricsProvider.provideNext(stepBase.name, MetricsProvider.TOTAL_NS);
        stepBase.maxNs = metricsProvider.provideNext(stepBase.name, MetricsProvider.MAX_NS);
        stepBase.avgNs = metricsProvider.provideNext(stepBase.name, MetricsProvider.AVG_NS);
        if (stepBase instanceof StepPredicate) {
            StepPredicate stepPredicate = (StepPredicate)stepBase;
            stepPredicate.evalTrue = metricsProvider.provideNext(stepPredicate.name, MetricsProvider.EVAL_TRUE);
            stepPredicate.evalFalse = metricsProvider.provideNext(stepPredicate.name, MetricsProvider.EVAL_FALSE);
        }
    }

    public StepBase buildComplexStatement(String type, String source, List<Map<String, Object>> config) {

        try {
            Class clazz = statementTypesMap.get(type);
            Class[] types = {String.class};
            Constructor constructor = clazz.getConstructor(types);
            Object[] parameters = {source};
            Object instance = constructor.newInstance(parameters);
            StepStatementComplex stepStatementComplex = (StepStatementComplex) instance;
            stepStatementComplex.setName(buildUniqueNameFromType(type+source));
            stepStatementComplex.subStatements = parse(config);

            setMetricsFromProvider(stepStatementComplex);

            return stepStatementComplex;
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public StepBase buildComplexPredicate(String type, List<Map<String, Object>> config) {

        try {
            Class clazz = predicateTypesMap.get(type);
            Class[] types = {};
            Constructor constructor = clazz.getConstructor(types);
            Object[] parameters = {};
            Object instance = constructor.newInstance(parameters);
            StepPredicateComplex stepPredicateComplex = (StepPredicateComplex) instance;
            stepPredicateComplex.setName(buildUniqueNameFromType(type));

            for (Map<String, Object> node : config) {
                String subType = firstKey(node);
                stepPredicateComplex.subPredicate.add(buildPredicate(subType, (Map<String, Object>) node.get(subType)));
            }
            setMetricsFromProvider(stepPredicateComplex);

            return stepPredicateComplex;
        }
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void replaceConfigWithEnvVars(Map<String, Object> config) {
        // 1) replace env vars in all configuration
        Map<String, String> env = System.getenv();
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            if (entry.getValue() instanceof String) {
                String replaced = StringSubstitutor.replace(entry.getValue(), env);
                config.replace(entry.getKey(), replaced);
            }
        }
    }

    public String buildUniqueNameFromName(String name) {
        if (userProvidedNames.contains(name)) {
            throw new IllegalArgumentException("duplicate name: " + name);
        }
        else {
            userProvidedNames.add(name);
            instanceCounter.increment();
            return name;
        }
    }

    public String buildUniqueNameFromType(String type) {
        instanceCounter.increment();
        String retVal = type + "_" + instanceCounter.longValue();
        return retVal;
    }

    public void buildUniqueNameFromType(String type, Map<String, Object> config) {
        if (config.containsKey("name")) {
            instanceCounter.increment();
        }
        else {
            instanceCounter.increment();
            config.put("name", type + "_" + instanceCounter.longValue());
        }
    }

    public LongAdder getInstanceCounter() {
        return instanceCounter;
    }

}
