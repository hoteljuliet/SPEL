package net.hoteljuliet.spel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.hoteljuliet.spel.predicates.And;
import net.hoteljuliet.spel.predicates.Not;
import net.hoteljuliet.spel.predicates.Or;
import net.hoteljuliet.spel.predicates.Xor;
import net.hoteljuliet.spel.statements.ForEach;
import org.apache.commons.text.StringSubstitutor;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

public class Factory {
    private final LongAdder instanceCounter;

    private final ObjectMapper objectMapper;

    private Map<String, Class> predicateTypesMap;
    private Map<String, Class> statementTypesMap;

    public Factory(String[] predicatePackages, String[] statementPackages) {
        instanceCounter = new LongAdder();
        predicateTypesMap = new HashMap<>();
        statementTypesMap = new HashMap<>();
        populateTypesMap(predicatePackages, predicateTypesMap);
        populateTypesMap(statementPackages, statementTypesMap);
        objectMapper = new ObjectMapper(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    private void populateTypesMap(String[] packages, Map<String, Class> typesMap) {
        for (String packageName : packages) {
            Reflections reflections = new Reflections(packageName);
            Set<Class<?>> stepTypes = reflections.getTypesAnnotatedWith(Step.class);
            for (Class clazz : stepTypes) {
                Step stepAnnotation = (Step) clazz.getAnnotation(Step.class);
                if (typesMap.containsKey(stepAnnotation.tag())) {
                    throw new RuntimeException("duplicate step tag: " + stepAnnotation.tag());
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
            return (StepBase) objectMapper.readValue(objectMapper.writeValueAsBytes(config), clazz);
        }
        else {
            throw new RuntimeException("no type found for: " + type);
        }
    }


    public StepBase buildComplexStatement(String type, String source, List<Map<String, Object>> config) {

        StepBase retVal;
        switch (type) {
            case "foreach": {
                ForEach forEach = new ForEach(source);
                for (Map<String, Object> node : config) {
                    String subStatementType = Parser.firstKey(node);
                    StepBase s = buildStatement(subStatementType, (Map<String, Object>) node.get(subStatementType));
                    forEach.subStatements.add(s);
                }
                retVal = forEach;
                break;
            }
            default : {
                throw new IllegalArgumentException("unknown step: " + type);
            }
        }
        retVal.setName(buildUniqueNameFromType(type));
        return retVal;
    }

    public StepBase buildComplexPredicate(String type, List<Map<String, Object>> config) {

        // TODO: give complex predicates the option for a friendly, unique name
        StepBase retVal;

        switch (type) {
            case "xor" : {
                Xor xor = new Xor();

                if (config.size() > 2) {
                    throw new IllegalArgumentException("xor max sub-predicate is 2");
                }
                else {
                    for (Map<String, Object> node : config) {
                        String subPredicateType = Parser.firstKey(node);
                        StepBase s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                        xor.subPredicate.add(s);
                    }
                    retVal = xor;
                    break;
                }
            }
            case "not" : {
                Not not = new Not();
                for (Map<String, Object> node : config) {
                    String subPredicateType = Parser.firstKey(node);
                    StepBase s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                    not.subPredicate.add(s);
                }
                retVal = not;
                break;
            }
            case "and" : {
                And and = new And();
                for (Map<String, Object> node : config) {
                    String subPredicateType = Parser.firstKey(node);
                    StepBase s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                    and.subPredicate.add(s);
                }
                retVal = and;
                break;
            }
            case "or" : {
                Or or = new Or();
                for (Map<String, Object> node : config) {
                    String subPredicateType = Parser.firstKey(node);
                    StepBase s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                    or.subPredicate.add(s);
                }
                retVal = or;
                break;
            }
            default : {
                throw new IllegalArgumentException("unknown step: " + type);
            }
        }
        retVal.setName(buildUniqueNameFromType(type));
        return retVal;
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
        instanceCounter.increment();
        return name;
    }

    public String buildUniqueNameFromType(String type) {
        String retVal = type + "_" + instanceCounter.longValue();
        instanceCounter.increment();
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
