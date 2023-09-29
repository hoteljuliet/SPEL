package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Step(tag = "flatten")
public class Flatten extends StepStatement {

    private String in;
    private String root;
    private String out;

    /**
     * Flatten a Map within the context
     * @param in a path to a Map in the context
     * @param root the symbol to use as the root of the flattened Map
     * @param out the path in the context where the flattened Map will be placed
     */
    @JsonCreator
    public Flatten(@JsonProperty(value = "in", required = true) String in,
                   @JsonProperty(value = "root", required = true) String root,
                   @JsonProperty(value = "out", required = true) String out) {
        super();
        this.in = in;
        this.root = root;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Map<String, Object> original = context.getField(in);
        Map<String, Object> flattened = new HashMap<>();
        flatten(root, original, flattened);
        context.addField(out, flattened);
        return EMPTY;
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
