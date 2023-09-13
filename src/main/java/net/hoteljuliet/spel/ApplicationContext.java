package net.hoteljuliet.spel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {

    private static final ObjectMapper objectMapper =
            new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);

    public static Map<String, Object> applicationContext;

    public static void initialize(List<String> scannerPrefixes) throws Exception {
        applicationContext = new HashMap<>();

        // gather all of the data from the beans
        for (String prefix : scannerPrefixes) {
            Reflections reflections = new Reflections(prefix);
            Set<Class<?>> beanTypes = reflections.getTypesAnnotatedWith(Bean.class);

            for (Class clazz : beanTypes) {
                Map<String, Object> beanValues = new HashMap<>();
                Object bean = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object fieldValue = field.get(bean);
                    beanValues.put(field.getName(), fieldValue);
                }
                applicationContext.put(clazz.getSimpleName(), beanValues);
            }
        }
    }

    public static void initialize(Object instance) throws Exception {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Value.class)) {
                Value value = field.getAnnotation(Value.class);
                field.setAccessible(true);

                Context context = new Context(applicationContext);
                for (String expression : value.exp()) {
                    Map<String, Object> node = objectMapper.readValue(expression, Map.class);
                    Step s = Parser.parse(node);
                    s.execute(context);
                }
                Object fieldValue = context.getField(field.getDeclaringClass().getSimpleName() + "." + field.getName());
                field.set(instance, fieldValue);
            }
        }
    }
}
