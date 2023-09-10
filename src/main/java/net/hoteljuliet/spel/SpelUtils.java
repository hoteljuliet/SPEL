package net.hoteljuliet.spel;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpelUtils {

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

}
