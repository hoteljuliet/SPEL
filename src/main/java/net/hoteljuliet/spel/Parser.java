package net.hoteljuliet.spel;

import net.hoteljuliet.spel.predicates.If;

import java.util.*;
import java.util.stream.Collectors;

public class Parser {

    public static List<Step> build(List<Map<String, Object>> config) {
        if (config == null) {
            throw new RuntimeException("empty config");
        }
        else {
            return config.stream().map(Parser::parse).collect(Collectors.toList());
        }
    }

    public static Step parse(Map<String, Object> node) {
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

    public static Step parsePredicate(Optional<String> name, Map<String, Object> node) {

        Object firstValue = firstValue(node);
        String type = firstKey(node);

        List<Map<String, Object>> onTrue = (List<Map<String, Object>>) node.get("then");

        List<Map<String, Object>> onFalse = new ArrayList<>();
        if (node.containsKey("else")) {
            onFalse = (List<Map<String, Object>>) node.get("else");
        }

        If ifPredicate = new If();

        Step predicate = null;
        // this is an "and" or "or" type predicate (one with multiple sub-predicates)
        if (firstValue instanceof List) {
            predicate = Factory.buildComplexPredicate(type, (List<Map<String, Object>>) node.get(type));
        }
        // this is a simple/single predicate
        else {
            predicate = Factory.buildPredicate(type, (Map<String, Object>) node.get(type));
        }
        ifPredicate.predicate = predicate;

        for (Map<String, Object> map : onTrue) {
            Step s = parse(map);
            ifPredicate.onTrue.add(s);
        }

        for (Map<String, Object> map : onFalse) {
            Step s = parse(map);
            ifPredicate.onFalse.add(s);
        }

        if (name.isPresent()) {
            ifPredicate.setName(Factory.buildUniqueNameFromName(name.get()));
        }
        else {
            ifPredicate.setName(Factory.buildUniqueNameFromType("if"));
        }

        return ifPredicate;
    }

    public static Step parseStatement(Map<String, Object> node) {
        String type = firstKey(node);
        return Factory.buildStatement(type, (Map<String, Object>) node.get(type));
    }

    public static String firstKey(Map<String, Object> node) {
        return node.entrySet().iterator().next().getKey();
    }

    public static Object firstValue(Map<String, Object> node) {
        return node.entrySet().iterator().next().getValue();
    }
}
