package net.hoteljuliet.spel.mustache;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.hoteljuliet.spel.Context;

import java.io.Serializable;

/**
 * Typically, Steps are just dealing with the paths to values. I.e., base64(x.y.z). We would not
 * expect someone to base64 encode a constant value - they could just add that as a literal.
 *
 * In other cases, a value could be a constant/literal value or "the value of a field in the context".
 * This is useful in many situations to make the Steps more flexible - For Example: "In":
 * Using 2 TemplateLiterals, that one Step can do either:
 * 1) "x" (literal object) is in {{mylist}} (from context)
 * 2) {{myValue}} (from context) is in [a, b, c, d] (literal list)
 * 3) {{myValue}} (from context) is in {{mylist}} (from context)
 *
 * ^ this lets the user specify a path to a value or a literal value in any order they choose.
 */
public class TemplateLiteral implements Serializable {

    private Object object;
    private Boolean isTemplate;

    @JsonCreator
    public TemplateLiteral(Object object) {
        this.object = object;
        isTemplate = false;

        if (object instanceof String) {
            String string = String.valueOf(object);
            // if this is a template
            isTemplate = MustacheUtils.isTemplate(string);
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
            String path = MustacheUtils.trimMustache(object);
            return context.getField(path);
        }
        else {
            return (T) object;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.object);
    }
}
