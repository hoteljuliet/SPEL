package net.hoteljuliet.spel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.hoteljuliet.spel.predicates.And;
import net.hoteljuliet.spel.predicates.Or;
import org.apache.commons.text.CaseUtils;

import java.util.List;
import java.util.Map;

public class Factory {
    private static final ObjectMapper objectMapper =
            new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);

    public static Step buildStatement(String type, Map<String, Object> config) {
        try {
            Class clazz = Class.forName("net.hoteljuliet.spel.statements." + CaseUtils.toCamelCase(type, true, ' '));
            return (Step) objectMapper.readValue(objectMapper.writeValueAsBytes(config), clazz);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Step buildPredicate(String type, Map<String, Object> config) {
        try {
            Class clazz = Class.forName("net.hoteljuliet.spel.predicates." + CaseUtils.toCamelCase(type, true, ' '));
            return (Step) objectMapper.readValue(objectMapper.writeValueAsBytes(config), clazz);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Step buildComplexPredicate(String type, List<Map<String, Object>> config) {

        switch (type) {
            case "and" : {
                And and = new And();
                for (Map<String, Object> node : config) {
                    String subPredicateType = Parser.firstKey(node);
                    Step s = buildStatement(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                    and.predicate.add(s);
                }
                return and;
            }
            case "or" : {
                Or or = new Or();
                for (Map<String, Object> node : config) {
                    String subPredicateType = Parser.firstKey(node);
                    Step s = buildStatement(subPredicateType, (Map<String, Object>) node.get(subPredicateType));
                    or.predicate.add(s);
                }
                return or;
            }
            default : {
                throw new IllegalArgumentException("unknown step: " + type);
            }
        }
    }
}
