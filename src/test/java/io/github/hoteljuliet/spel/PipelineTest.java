package io.github.hoteljuliet.spel;

import org.junit.Test;

import java.io.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PipelineTest {

    @Test
    public void test_serializable() throws IOException, ClassNotFoundException {
        Pipeline pipeline = Pipeline.fromResource("/test_pipeline.yaml");
        pipeline.parse();

        Context context = new Context(pipeline);
        pipeline.execute(context);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(pipeline);
        objectOutputStream.flush();
        objectOutputStream.close();
        byte[] serialized = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        System.out.println("pipeline size in KB: " + serialized.length / 1024);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serialized);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Pipeline deserialized = (Pipeline) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();

        stopWatch.stop();
        System.out.println("round trip time: " + (stopWatch.getNanoTime() / 1000000)  + "ms");
        assertThat(deserialized.getBaseSteps().size()).isEqualTo(pipeline.getBaseSteps().size());
    }

    @Test
    public void testExamplePipeline() {
        Pipeline pipeline = Pipeline.fromResource("/test_pipeline.yaml");
        Integer numStepsParsed = pipeline.parse();

        //assertThat(numStepsParsed).isEqualTo(95);

        for (int i = 0; i <= 32; i++) {
            Context context = new Context(pipeline);
            pipeline.execute(context);
            assertThat(context.pipelineResult.getTotalSoftFailures()).isEqualTo(0);
        }

        System.out.println("Average Millis: " + pipeline.getRunTimeNanos().getMean() / 1000000);
        System.out.println("Min Millis: " + pipeline.getRunTimeNanos().getMin() / 1000000);
        System.out.println("Max Millis: " + pipeline.getRunTimeNanos().getMax() / 1000000);
        System.out.println("Total Millis: " + pipeline.getRunTimeNanos().getSum() / 1000000);

        System.out.println("Stack Traces: " + pipeline.getStackTraces().getMap());
        assertThat(pipeline.getStackTraces().getMap()).isEmpty();
        assertThat(pipeline.getTotalFailures().intValue()).isEqualTo(0);
    }

    @Test
    public void testFind() {
        Pipeline pipeline = Pipeline.fromResource("/test_pipeline.yaml");
        Integer numStepsParsed = pipeline.parse();

        Context context = new Context(pipeline);
        pipeline.execute(context);

        Optional<StepBase> value = pipeline.find("good");
        assertThat(value.isPresent()).isTrue();
        assertThat(value.get().name).isEqualTo("good");
    }

    @Test
    public void testMermaid() {
        Pipeline pipeline = Pipeline.fromResource("/test_pipeline.yaml");
        Integer numStepsParsed = pipeline.parse();

        Context context = new Context(pipeline);
        pipeline.execute(context);

        String mermaid = pipeline.toMermaid();
        System.out.println(mermaid);
    }
}
