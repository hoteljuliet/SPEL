package net.hoteljuliet.spel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.hoteljuliet.spel.predicates.And;
import net.hoteljuliet.spel.predicates.Not;
import net.hoteljuliet.spel.predicates.Or;
import org.apache.commons.text.CaseUtils;
import org.apache.commons.text.StringSubstitutor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public class Factory {
    private static final LongAdder instanceCounter = new LongAdder();

    private static final ObjectMapper objectMapper =
            new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);

    public static Step buildStatement(String type, Map<String, Object> config) {
        try {
            replaceConfigWithEnvVars(config);
            buildUniqueName(type, config);

            Class clazz = Class.forName("net.hoteljuliet.spel.statements." + CaseUtils.toCamelCase(type, true, '-'));
            return (Step) objectMapper.readValue(objectMapper.writeValueAsBytes(config), clazz);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Step buildPredicate(String type, Map<String, Object> config) {
        try {
            replaceConfigWithEnvVars(config);
            buildUniqueName(type, config);

            Class clazz = Class.forName("net.hoteljuliet.spel.predicates." + CaseUtils.toCamelCase(type, true, '-'));
            return (Step) objectMapper.readValue(objectMapper.writeValueAsBytes(config), clazz);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // TODO: forEach
    public static Step buildComplexStatement(String type, List<Map<String, Object>> config) {
        return null;
    }

    public static Step buildComplexPredicate(String type, List<Map<String, Object>> config) {

        // TODO: give complex predicates the option for a friendly, unique name
        Step retVal;

        switch (type) {
            case "not" : {
                Not not = new Not();
                for (Map<String, Object> node : config) {
                    String subPredicateType = Parser.firstKey(node);
                    Step s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                    not.predicate.add(s);
                }
                retVal = not;
                break;
            }
            case "and" : {
                And and = new And();
                for (Map<String, Object> node : config) {
                    String subPredicateType = Parser.firstKey(node);
                    Step s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                    and.predicate.add(s);
                }
                retVal = and;
                break;
            }
            case "or" : {
                Or or = new Or();
                for (Map<String, Object> node : config) {
                    String subPredicateType = Parser.firstKey(node);
                    Step s = buildPredicate(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                    or.predicate.add(s);
                }
                retVal = or;
                break;
            }
            default : {
                throw new IllegalArgumentException("unknown step: " + type);
            }
        }
        // TODO: retVal.setName(name);
        return retVal;
    }

    public static void replaceConfigWithEnvVars(Map<String, Object> config) {
        // 1) replace env vars in all configuration
        Map<String, String> env = System.getenv();
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            if (entry.getValue() instanceof String) {
                String replaced = StringSubstitutor.replace(entry.getValue(), env);
                config.replace(entry.getKey(), replaced);
            }
        }
    }

    public static String buildUniqueName(String type) {
        String retVal = type + instanceCounter.longValue();
        instanceCounter.increment();
        return retVal;
    }

    public static void buildUniqueName(String type, Map<String, Object> config) {

        if (config.containsKey("name")) {
            instanceCounter.increment();
        }
        else {
            instanceCounter.increment();
            config.put("name", type + instanceCounter.longValue());
        }
    }
}
