package net.hoteljuliet.spel.mustache;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import net.hoteljuliet.spel.mustache.UnescapedMustacheFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MustacheUtils {

    private final static Pattern mustachePattern = Pattern.compile("\\{\\{(.+?)\\}\\}");

    private final static MustacheFactory mustacheFactory = new UnescapedMustacheFactory();

    public static Mustache compile(String exp) {
        return mustacheFactory.compile(new StringReader(exp), "");
    }

    public static List<String> findVariables(String expression) {
        List<String> variables = new ArrayList<>();
        Matcher matcher = mustachePattern.matcher(expression);
        while (matcher.find()) {
            variables.add(matcher.group(1));
        }
        return variables;
    }

    public static Boolean isTemplate(String string) {
        return (string.contains("{{") && string.contains("}}"));
    }

    public static String trimMustache(Object expression) {
        return String.valueOf(expression).replaceAll("\\{\\{|\\}\\}", "");
    }
}
