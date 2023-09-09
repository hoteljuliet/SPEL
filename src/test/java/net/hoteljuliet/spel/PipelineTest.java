package net.hoteljuliet.spel;


import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
public class PipelineTest {

    @Test
    public void test1() throws IOException {
        Pipeline pipeline = Pipeline.fromResource("/test1.yaml");
        pipeline.build();

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

    @Test
    public void test2() throws IOException {
        Pipeline pipeline = Pipeline.fromResource("/test2.yaml");
        pipeline.build();

        Context context = new Context();
        pipeline.execute(context);
        assertThat(context.size()).isEqualTo(1);

        Optional<Integer> added = context.getField("x.y");
        assertThat(added.isPresent()).isTrue();
        assertThat(added.get()).isEqualTo(1);

    }


    @Test
    public void test3() throws IOException {
        Pipeline pipeline = Pipeline.fromResource("/test3.yaml");
        pipeline.build();

        Context context = new Context();
        pipeline.execute(context);

        System.out.println(context);

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

}
