package net.hoteljuliet.spel;

import net.hoteljuliet.spel.predicates.If;

import java.util.*;
import java.util.stream.Collectors;

public class Parser {

    private Factory factory;

    public Parser() {
        factory = new Factory(Pipeline.defaultPredicatePackages, Pipeline.defaultStatementPackages);
    }

    public Parser(String[] predicatePackages, String[] statementPackages) {
        factory = new Factory(predicatePackages, statementPackages);
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
            predicate = factory.buildComplexPredicate(type, (List<Map<String, Object>>) node.get(type));
        }
        // this is a simple/single predicate
        else {
            predicate = factory.buildPredicate(type, (Map<String, Object>) node.get(type));
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
            ifPredicate.setName(factory.buildUniqueNameFromName(name.get()));
        }
        else {
            ifPredicate.setName(factory.buildUniqueNameFromType("if"));
        }

        return ifPredicate;
    }

    public StepBase parseStatement(Map<String, Object> node) {
        String type = firstKey(node);

        if (type.startsWith("foreach")) {
            String[] parts = type.split("-");
            String typeName = parts[0];
            String source = parts[1];
            return factory.buildComplexStatement(typeName, source, (List<Map<String, Object>>) node.get(type));
        }
        else {
            return factory.buildStatement(type, (Map<String, Object>) node.get(type));
        }
    }

    public static String firstKey(Map<String, Object> node) {
        return node.entrySet().iterator().next().getKey();
    }

    public static Object firstValue(Map<String, Object> node) {
        return node.entrySet().iterator().next().getValue();
    }

    public Factory getFactory() {
        return factory;
    }
}
