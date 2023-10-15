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

/**
 * Context that contains application data in memory in a Map<String, Object> structure.
 * Provides three initialization methods
 */
public class ApplicationContext {

    private static final ObjectMapper objectMapper =
            new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);

    public static Map<String, Object> applicationContext;

    /**
     * Initialize ApplicationContext from Bean classes. Initialize the map with the class names as keys and an
     * inner map of field values for properties of the class.
     * @param beanPackages list of packages containing Java Beans
     * @throws Exception exceptions encountered in initialization
     */
    public static void initialize(List<String> beanPackages) throws Exception {
        applicationContext = new HashMap<>();

        // gather all of the data from the beans
        for (String prefix : beanPackages) {
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

    /**
     * Initialize the application context from a generic object using default predicate packages and statement packages
     * from the Pipeline class. This would include net.hoteljuliet.spel.predicates and net.hoteljuliet.spel.statements
     * @param instance an object instance
     * @throws Exception a generic exception
     */
    public static void initialize(Object instance) throws Exception {
        initialize(instance, Pipeline.defaultPredicatePackages, Pipeline.defaultStatementPackages);
    }

    /**
     * Initialize the application context from a generic object. Takes a string of predicate packages and statement
     * packages. Executes a base step on the loaded application context from expressions from the fields in the object
     * that have the Value annotation. The provided package and statement packages are used for parsing the
     * Map<String, Object> that Jackson's ObjectMapper populates from the object's field values.
     * The initialization function then sets field with the resolved fieldValue from the executed base step.
     * @param instance an object instance
     * @param predicatePackages a list of packages of predicates
     * @param statementPackages a list of packages for statements
     * @throws Exception any Exception
     */

    public static void initialize(Object instance, String[] predicatePackages, String[] statementPackages) throws Exception {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Value.class)) {
                Value value = field.getAnnotation(Value.class);
                field.setAccessible(true);

                Context context = new Context(applicationContext);
                for (String expression : value.exp()) {
                    Map<String, Object> node = objectMapper.readValue(expression, Map.class);
                    Parser parser = new Parser(predicatePackages, statementPackages);
                    BaseStep s = parser.parse(node);
                    s.execute(context);
                }
                Object fieldValue = context.getField(field.getDeclaringClass().getSimpleName() + "." + field.getName());
                field.set(instance, fieldValue);
            }
        }
    }
}
