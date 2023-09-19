package net.hoteljuliet.spel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.github.mustachejava.Mustache;

import java.io.Serializable;
import java.util.Optional;

public class TemplateLiteral implements Serializable {

    private Object object;
    private Boolean isTemplate;
    private transient Mustache template;

    @JsonCreator
    public TemplateLiteral(Object object) {
        this.object = object;
        isTemplate = false;

        if (object instanceof String) {
            String string = String.valueOf(object);
            // if this is a template
            isTemplate = (string.contains("{{") && string.contains("}}"));
        }
    }

    public Object get(Context context) {
        if (isTemplate) {
            if (null == template) {
                template = MustacheUtils.compile(String.valueOf(object));
            }
            return context.render(template);
        }
        else {
            return object;
        }
    }
}
