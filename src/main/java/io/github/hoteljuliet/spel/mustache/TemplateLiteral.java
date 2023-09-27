package io.github.hoteljuliet.spel.mustache;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.github.hoteljuliet.spel.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class that makes predicate/step configuration easier. Only use when a configuration could be from
 * the Context or a literal value.
 *
 * Typically, Steps are just dealing with the paths to values. I.e., base64(x.y.z).
 *
 * In other cases, a value could be a constant/literal value OR "the value of a field in the context".
 * This is useful in many situations to make the Steps more flexible - For Example: "In":
 * Using 2 TemplateLiterals, that one Step can do either:
 * 1) "x" (literal object) is in {{mylist}} (from context)
 * 2) {{myValue}} (from context) is in [a, b, c, d] (literal list)
 * 3) {{myValue}} (from context) is in {{mylist}} (from context)
 * ^ this class lets the user specify a path to a value or a literal value, and in any order/combo they choose.
 */

public class TemplateLiteral implements Serializable {

    private final static Pattern mustachePattern = Pattern.compile("\\{\\{(.+?)\\}\\}");

    private Object object;
    private Boolean isTemplate;

    @JsonCreator
    public TemplateLiteral(Object object) {
        this.object = object;
        isTemplate = false;

        if (object instanceof String) {
            String string = String.valueOf(object);
            // if this is a template
            isTemplate = isTemplate(string);
        }
    }

    /**
     *
     * @param context
     * @param <T>
     * @return
     */
    public <T> T get(Context context) {

        if (isTemplate) {
            String path = trimMustache(object);
            return context.getField(path);
        }
        else {
            return (T) object;
        }
    }

    public List<String> getVariables() {
        String string = String.valueOf(object);
        List<String> variables = new ArrayList<>();
        Matcher matcher = mustachePattern.matcher(string);
        while (matcher.find()) {
            variables.add(matcher.group(1));
        }
        return variables;
    }

    @Override
    public String toString() {
        return String.valueOf(this.object);
    }

    public static Boolean isTemplate(String string) {
        return (string.contains("{{") && string.contains("}}"));
    }

    public static String trimMustache(Object expression) {
        return String.valueOf(expression).replaceAll("\\{\\{|\\}\\}", "");
    }
}
