package net.hoteljuliet.spel;

import org.junit.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
public class PipelineTest {

    @Test
    public void test_serializable() throws IOException, ClassNotFoundException {
        Pipeline pipeline = Pipeline.fromResource("/test_pipeline.yaml");
        pipeline.parse();

        Context context = new Context();
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
    public void testExamplePipeline() throws IOException {
        PipelineMetrics pipelineMetrics = new PipelineMetrics();
        Pipeline pipeline = Pipeline.fromResource("/test_pipeline.yaml");
        Integer numStepsParsed = pipeline.parse();
        for (int i = 0; i < 32; i++) {
            Context context = new Context();
            pipeline.execute(context, pipelineMetrics);
        }
        System.out.println("Average Millis: " + pipelineMetrics.getAverageMillis());
        System.out.println("Min Millis: " + pipelineMetrics.getMinMillis());
        System.out.println("Max Millis: " + pipelineMetrics.getMaxMillis());
        System.out.println("Total Millis: " + pipelineMetrics.getTotalMillis());
    }
}
