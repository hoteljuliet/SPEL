package net.hoteljuliet.spel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.hoteljuliet.spel.predicates.And;
import net.hoteljuliet.spel.predicates.Not;
import net.hoteljuliet.spel.predicates.Or;
import net.hoteljuliet.spel.predicates.Xor;
import net.hoteljuliet.spel.statements.ForEach;
import org.apache.commons.text.CaseUtils;
import org.apache.commons.text.StringSubstitutor;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

/**
 * Factory builds complex statements, predicates, and base steps
 */
public class Factory {
    private final LongAdder instanceCounter = new LongAdder();

    private final ObjectMapper objectMapper =
            new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);

    private Map<String, Class> predicateTypesMap;
    private Map<String, Class> statementTypesMap;

    /**
     * Constructs a Factory
     * @param predicatePackages packages that contains predicate definitions
     * @param statementPackages packages that contain statement definitions
     */
    public Factory(String[] predicatePackages, String[] statementPackages) {
        predicateTypesMap = new HashMap<>();
        statementTypesMap = new HashMap<>();
        populateTypesMap(predicatePackages, predicateTypesMap);
        populateTypesMap(statementPackages, statementTypesMap);
    }

    /**
     * Populates a map mapping Strings to its class. Loads Step classes from a list of packages.
     * @param packages String list of packages to scan
     * @param typesMap A Map<String, Class> object to populate
     * @throws RuntimeException if duplicate step classes are found in a package
     */
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

    /**
     * Builds a BaseStep from a passes statement type and a configuration map to substitute environment variables
     * @param type statement type
     * @param config configuration for building the base step
     * @return a BaseStep representing the statement
     */
    public BaseStep buildStatement(String type, Map<String, Object> config) {
        try {
            replaceConfigWithEnvVars(config);
            buildUniqueNameFromType(type, config);
            return buildBaseStep(type, config, statementTypesMap);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    /**
     * Builds a BaseStep from a passed predicate type and a configuration map to substitute environment variables
     * @param type predicate type
     * @param config configuration for building the base step
     * @return a BaseStep representing the predicate
     */
    public BaseStep buildPredicate(String type, Map<String, Object> config) {
        try {
            replaceConfigWithEnvVars(config);
            buildUniqueNameFromType(type, config);
            return buildBaseStep(type, config, predicateTypesMap);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Builds a basestep from a passed type
     * @param type step type
     * @param config used to build the base step
     * @param typesMap used to fetch the class of the type
     * @return a BaseStep from a passed type
     * @throws RuntimeException for any types not found for the passed type
     */
    public BaseStep buildBaseStep(String type, Map<String, Object> config, Map<String, Class> typesMap) throws Exception {
        if (typesMap.containsKey(type)) {
            Class clazz = typesMap.get(type);
            return (BaseStep) objectMapper.readValue(objectMapper.writeValueAsBytes(config), clazz);
        }
        else {
            throw new RuntimeException("no type found for: " + type);
        }
    }
    /**
     * Builds a complex statement given its type, currently supporting 'foreach'
     * @param type type of statement
     * @param config list of configuration maps
     * @return a BaseStep representing a complex statement
     *
     */

    public BaseStep buildComplexStatement(String type, String source, List<Map<String, Object>> config) {

        BaseStep retVal;
        switch (type) {
            case "foreach": {
                ForEach forEach = new ForEach(source);
                for (Map<String, Object> node : config) {
                    String subStatementType = Parser.firstKey(node);
                    BaseStep s = buildStatement(subStatementType, (Map<String, Object>) node.get(subStatementType));
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

    /**
     * Builds a complex predicate given its type, currently supporting 'xor', 'or', 'and', 'not'
     * @param type type of predicate
     * @param config list of configuration maps
     * @return a BaseStep representing a complex predicate
     *
     */
    public BaseStep buildComplexPredicate(String type, List<Map<String, Object>> config) {

        // TODO: give complex predicates the option for a friendly, unique name
        BaseStep retVal;

        switch (type) {
            case "xor" : {
                Xor xor = new Xor();

                if (config.size() > 2) {
                    throw new IllegalArgumentException("xor max sub-predicate is 2");
                }
                else {
                    for (Map<String, Object> node : config) {
                        String subPredicateType = Parser.firstKey(node);
                        BaseStep s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
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
                    BaseStep s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                    not.subPredicate.add(s);
                }
                retVal = not;
                break;
            }
            case "and" : {
                And and = new And();
                for (Map<String, Object> node : config) {
                    String subPredicateType = Parser.firstKey(node);
                    BaseStep s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                    and.subPredicate.add(s);
                }
                retVal = and;
                break;
            }
            case "or" : {
                Or or = new Or();
                for (Map<String, Object> node : config) {
                    String subPredicateType = Parser.firstKey(node);
                    BaseStep s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
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

    /**
     * Replaces string instances of values with the environment variable equivalent
     * @param config configuration map
     */
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

    /**
     * increments the instance counter and returns the passed name
     * @param name name of the instance
     * @return the passed name parameter
     */
    public String buildUniqueNameFromName(String name) {
        instanceCounter.increment();
        return name;
    }

    /**
     * Builds a unique name from a given statement/predicate type
     * @param type type to build a name for
     * @return a concatenation of the type and the count of the number of instantiations of this class
     */
    public String buildUniqueNameFromType(String type) {
        String retVal = type + "[" + instanceCounter.longValue() + "]";
        instanceCounter.increment();
        return retVal;
    }

    /**
     * Maps 'name' to a unique type concatenated with the count of the number of instances of 'name'
     * e.g 'name' -> Clazz[2]
     * If the config contains 'name', the instanceCounter variable is incremented.
     * @param type string representation of a type
     * @param config configuration Map<String, Object>
     */

    public void buildUniqueNameFromType(String type, Map<String, Object> config) {

        if (config.containsKey("name")) {
            instanceCounter.increment();
        }
        else {
            instanceCounter.increment();
            config.put("name", type + "[" + instanceCounter.longValue() + "]");
        }
    }
}
