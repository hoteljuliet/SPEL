package net.hoteljuliet.spel;


import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import net.hoteljuliet.spel.statements.Flatten;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
public class PipelineTest {

    /*
    @Test
    public void test_serializable() throws IOException {
        Pipeline pipeline = Pipeline.fromResource("/test3.yaml");
        pipeline.parse();

        Context context = new Context();
        pipeline.execute(context);

        StopWatch stopWatch = new StopWatch();
        stopWatch.reset();
        stopWatch.start();
        String snapShotB64 = pipeline.snapshot();
        Pipeline restored = Pipeline.restore(snapShotB64);
        stopWatch.stop();

        System.out.println("round trip time: " + stopWatch.getTime() + "ms");

        restored.execute(context);
    }
     */

    @Ignore
    @Test
    public void test1() throws IOException {
        Pipeline pipeline = Pipeline.fromResource("/test1.yaml");
        pipeline.parse();

        Context context = new Context();
        pipeline.execute(context);
        assertThat(context.size()).isEqualTo(1);

        Optional<String> added = context.getField("x.y");
        assertThat(added.isPresent()).isTrue();
        assertThat(added.get().equalsIgnoreCase("1")).isTrue();

        // TODO: refactor
        //assertThat(pipeline.getSteps().get(0).success.intValue()).isEqualTo(1);
        //assertThat(pipeline.getSteps().get(0).otherFailure.intValue()).isEqualTo(0);
        //assertThat(pipeline.getSteps().get(0).runTimeNanos.getMax()).isGreaterThan(0);

        //assertThat(pipeline.getSteps().get(1).success.intValue()).isEqualTo(1);
        //assertThat(pipeline.getSteps().get(1).otherFailure.intValue()).isEqualTo(0);
        //assertThat(pipeline.getSteps().get(1).runTimeNanos.getMax()).isGreaterThan(0);
    }

    @Ignore
    @Test
    public void test2() throws IOException {
        Pipeline pipeline = Pipeline.fromResource("/test2.yaml");
        pipeline.parse();

        Context context = new Context();
        pipeline.execute(context);
        assertThat(context.size()).isEqualTo(1);

        Optional<Integer> added = context.getField("x.y");
        assertThat(added.isPresent()).isTrue();
        assertThat(added.get()).isEqualTo(1);
    }

    @Test
    public void test3() throws IOException {

        for (int i = 0; i < 8; i++) {
            Pipeline pipeline = Pipeline.fromResource("/test3.yaml");
            Integer numStepsParsed = pipeline.parse();
            Context context = new Context();
            pipeline.execute(context);
            Map<String, StepMetrics> metrics = context.getField("_state.stepMetrics");
            assertThat(metrics.size()).isLessThanOrEqualTo(numStepsParsed);
        }
    }

    @Test
    public void test4() throws IOException {
        Map<String, Object> doc = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        doc.put("x", "y");
        doc.put("z", list);
        String template = "{{z.0}}";
        MustacheFactory mustacheFactory = new UnescapedMustacheFactory();
        Mustache mustache =  mustacheFactory.compile(new StringReader(template), "");
        StringWriter writer = new StringWriter();
        mustache.execute(writer, Arrays.asList(doc));
        writer.flush();
        System.out.println(writer.toString());
    }

    @Test
    public void test5() throws Exception {
        List<String> prefixes = new ArrayList<>();
        prefixes.add("net.hoteljuliet.spel");

        ApplicationContext.initialize(prefixes);

        ExampleValue exampleValue = new ExampleValue();
        ApplicationContext.initialize(exampleValue);
        assertThat(exampleValue.name).isEqualTo("dennis");

    }

    @Test
    public void test6() {
        Flatten flatten = new Flatten("", "", "");

        Map<String, Object> in = new HashMap<>();
        Map<String, Object> out = new HashMap<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("k1", "v1");
        map1.put("k2", "v2");

        List<String> list1 = new ArrayList<>();
        list1.add("list1Value");

        in.put("map1", map1);
        in.put("list1", list1);

        in.put("this", "that");

        flatten.flatten("root", in, out);

        System.out.println(out);
    }
}
