package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Step(tag = "flatten")
public class Flatten extends StepStatement {

    private String source;
    private String root;
    private String dest;

    @JsonCreator
    public Flatten(@JsonProperty(value = "source", required = true) String source,
                   @JsonProperty(value = "root", required = true) String root,
                   @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.source = source;
        this.root = root;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (context.containsKey(source)) {
            Map<String, Object> original = context.getField(source);
            Map<String, Object> flattened = new HashMap<>();
            flatten(root, original, flattened);
            context.addField(dest, flattened);
        }
        else {
            missingField.increment();
        }
        return NEITHER;
    }

    public void flatten(String key, Map<String, Object> in, Map<String, Object> out) {
        for (Map.Entry<String, Object> entry : in.entrySet()) {

            String newKey = key + "." + entry.getKey();

            if (entry.getValue() instanceof Map) {
                flatten(newKey, (Map<String, Object>) entry.getValue(), out);
            }
            else if (entry.getValue() instanceof List) {
                flattenList(newKey, (List<Object>) entry.getValue(), out);
            }
            else {
                out.put(newKey, entry.getValue());
            }
        }
    }

    private void flattenList(String key, List<Object> in, Map<String, Object> out)  {
        for (int i = 0; i < in.size(); i++) {
            String newKey = key + "["+ i + "]";
            out.put(newKey, in.get(i));
        }
    }
}
