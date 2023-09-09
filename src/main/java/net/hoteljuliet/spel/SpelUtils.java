package net.hoteljuliet.spel;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpelUtils {

    private final static Pattern mustachePattern = Pattern.compile("\\{\\{(.+?)\\}\\}");

    private final static MustacheFactory mustacheFactory = new UnescapedMustacheFactory();

    public static Mustache compile(String exp) {
        return mustacheFactory.compile(new StringReader(exp), "");
    }

    public static Set<String> findVariables(String expression) {
        Set<String> variables = new HashSet<>();
        Matcher matcher = mustachePattern.matcher(expression);

        while (matcher.find()) {
            variables.add(matcher.group(1));
        }
        return variables;
    }

}
