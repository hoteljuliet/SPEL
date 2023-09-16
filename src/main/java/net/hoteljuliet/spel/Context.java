package net.hoteljuliet.spel;

import com.github.mustachejava.Mustache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkState;

public class Context implements Map<String, Object> {

    private static final Logger logger = LoggerFactory.getLogger(Context.class);
    private Map<String, Object> backing;
    private List<BaseStep> executedBaseSteps;

    public Context() {
        executedBaseSteps = new ArrayList<>();
        this.backing = new HashMap<>();
    }

    public Context(Map<String, Object> backing) {
        this();
        this.backing = backing;
    }

    @Override
    public int size() {
        return backing.size();
    }

    @Override
    public boolean isEmpty() {
        return backing.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return backing.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return backing.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return backing.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return backing.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return backing.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        backing.putAll(m);
    }

    @Override
    public void clear() {
        backing.clear();
    }

    @Override
    public Set<String> keySet() {
        return backing.keySet();
    }

    @Override
    public Collection<Object> values() {
        return backing.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return backing.entrySet();
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        return backing.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        backing.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
        backing.replaceAll(function);
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        return backing.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return backing.remove(key, value);
    }

    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        return backing.replace(key, oldValue, newValue);
    }

    @Override
    public Object replace(String key, Object value) {
        return backing.replace(key, value);
    }

    @Override
    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        return backing.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return backing.computeIfPresent(key, remappingFunction);
    }

    @Override
    public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        return backing.compute(key, remappingFunction);
    }

    @Override
    public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        return backing.merge(key, value, remappingFunction);
    }

    /**
     * Context access utility methods
     */
    public boolean hasField(String path) {
        Optional<Object> field = getByPath(backing, path);
        return field.isPresent();
    }

    public boolean hasFields(String... paths) {
        boolean retVal = true;
        for (String path : paths) {
            Optional<Object> field = getByPath(backing, path);
            if (!field.isPresent()) {
                retVal = false;
                break;
            }
        }
        return retVal;
    }

    public boolean hasField(String path, Class clazz) {
        Optional<Object> field = getByPath(backing, path);
        return field.isPresent() && clazz.isInstance(field.get());
    }

    public <T> T getField(String path) {
        Optional<Object> field = getByPath(backing, path);
        checkState(field.isPresent(), "Couldn't resolve field in path [%s]", path);
        return (T) field.get();
    }

    public void addField(String path, Object value) {
        Map<String, Object> context = backing;
        List<String> pathElements = tokenizePath(path);

        String leafKey = pathElements.get(pathElements.size() - 1);

        for (String pathElement : pathElements.subList(0, pathElements.size() - 1)) {
            Object pathValue = context.get(pathElement);
            if (pathValue != null && pathValue instanceof Map) {
                context = (Map) pathValue;
            } else {
                Map<String, Object> newMap = new HashMap<>();
                context.put(pathElement, newMap);
                context = newMap;
            }
        }
        context.put(leafKey, value);
    }

    /**
     * removes field from source
     * @param path
     * @return {@code true} if field has been removed
     *         {@code false} if field wasn't exist
     */
    public boolean removeField(String path) {
        if (!hasField(path)) {
            return false;
        }
        Map<String, Object> context = backing;
        List<String> pathElements = tokenizePath(path);

        List<String> pathElementsWithoutLeaf = pathElements.subList(0, pathElements.size() - 1);
        String leafKey = pathElements.get(pathElements.size() - 1);

        for (String currElement : pathElementsWithoutLeaf) {
            context = (Map<String, Object>) context.get(currElement);
        }

        context.remove(leafKey);

        return true;
    }

    public void appendList(String path, Object value) {
        List<Object> list;
        if (!hasField(path)) {
            addField(path, new ArrayList<>());
        }

        Object field = getField(path);
        if (field instanceof List) {
            list = (List) field;
        } else {
            list = new ArrayList<>();
            list.add(field);
            removeField(path);
            addField(path, list);
        }
        if (value instanceof List) {
            list.addAll((List)value);
        } else {
            list.add(value);
        }
    }

    /**
     * removes value from a list
     * @param path
     * @param value
     * @return {@code true} if value removed from list
     *         {@code false} otherwise
     */
    public boolean removeFromList(String path, Object value) {
        if (!hasField(path)) {
            return false;
        }

        Object field = getField(path);
        if (field instanceof List) {
            List<Object> list = (List) field;

            if (value instanceof List) {
                list.removeAll((List) value);
            } else {
                list.remove(value);
            }

            return true;
        }
        return false;
    }

    /**
     * json OGNL (Object Graph Navigation Language) getter.
     * <p>for example:
     * <pre>
     * JsonUtils.getByPath(json, "x.y.z")
     * </pre>
     *
     * @return Optional of the value in paths
     * @throws Exception on any error
     **/
    private static <T> Optional<T> getByPath(Map json, String... paths) {
        Object cursor = json;
        for (String path : paths) {
            for (String pathElement : tokenizePath(path)) {
                if (!(cursor instanceof Map)) return Optional.empty();

                cursor = ((Map) cursor).get(pathElement);
                if (cursor == null) return Optional.empty();
            }
        }
        return Optional.of((T) cursor);
    }

    private static List<String> tokenizePath(String s) {
        List<String> pathTokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inEscape = false;

        for (char c : s.toCharArray()) {
            if (inEscape) {
                inEscape = false;
                sb.append(c);
            } else if (c == '\\') {
                inEscape = true;
            } else if (c == '.') {
                pathTokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        pathTokens.add(sb.toString());
        return pathTokens;
    }
    public boolean replaceFieldValue(String path,Object newValue){
        if (hasField(path)) {
            removeField(path);
            addField(path, newValue);
            return true;
        }
        return false;
    }

    public void replace(Map<String,Object> otherMap){
        backing.clear();
        backing.putAll(otherMap);
    }

    @Override
    public String toString() {
        return "Context: " + backing.toString();
    }

    public String render(Mustache mustache) {
        Object docContext = backing;
        StringWriter writer = new StringWriter();
        mustache.execute(writer, Arrays.asList(docContext));
        writer.flush();
        return writer.toString();
    }

    public List<BaseStep> getExecutedBaseSteps() {
        return executedBaseSteps;
    }
}
