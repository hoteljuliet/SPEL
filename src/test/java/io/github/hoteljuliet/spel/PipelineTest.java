package io.github.hoteljuliet.spel;

import io.github.hoteljuliet.spel.metrics.DefaultMetricsProvider;
import io.github.hoteljuliet.spel.metrics.MetricsProvider;
import org.junit.Test;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

public class PipelineTest {

    @Test
    public void test_serializable() throws IOException, ClassNotFoundException {
        Pipeline pipeline = Pipeline.fromResource("/test_pipeline.yaml");
        pipeline.parse(new DefaultMetricsProvider());

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
        DefaultMetricsProvider metricsProvider = new DefaultMetricsProvider();
        Integer numStepsParsed = pipeline.parse(metricsProvider);
        assertThat(numStepsParsed).isEqualTo(109);

        for (int i = 0; i <= 32; i++) {
            Context context = new Context(pipeline);
            pipeline.execute(context);
            // assert pipeline threw no exceptions
            assertThat(metricsProvider.getMetric(pipeline.name, MetricsProvider.EXCEPTION).get()).isEqualTo(0);

            // assert no steps had a soft failure
            Map<String, AtomicLong> softFails = metricsProvider.getAllByMetricName(MetricsProvider.SOFT_FAIL);
            for (Map.Entry<String, AtomicLong> entry : softFails.entrySet()) {
                assertThat(entry.getValue().get()).isEqualTo(0).withFailMessage( "%s has %d soft failures", entry.getKey(), entry.getValue().get());
            }

            // assert no steps had an exception
            Map<String, AtomicLong> exceptions = metricsProvider.getAllByMetricName(MetricsProvider.EXCEPTION);
            for (Map.Entry<String, AtomicLong> entry : exceptions.entrySet()) {
                assertThat(entry.getValue().get()).isEqualTo(0).withFailMessage( "%s has %d exceptions", entry.getKey(), entry.getValue().get());
            }
        }

        Map<String, AtomicLong> pipelineMetrics = metricsProvider.getAllByName(pipeline.name);
        for (Map.Entry<String, AtomicLong> entry : pipelineMetrics.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    @Test
    public void testFind() {
        Pipeline pipeline = Pipeline.fromResource("/test_pipeline.yaml");
        Integer numStepsParsed = pipeline.parse(new DefaultMetricsProvider());

        Context context = new Context(pipeline);
        pipeline.execute(context);

        Optional<StepBase> value = pipeline.find("good");
        assertThat(value.isPresent()).isTrue();
        assertThat(value.get().name).isEqualTo("good");
    }

    @Test
    public void testMermaid() {
        Pipeline pipeline = Pipeline.fromResource("/test_pipeline.yaml");
        Integer numStepsParsed = pipeline.parse(new DefaultMetricsProvider());

        Context context = new Context(pipeline);
        pipeline.execute(context);

        String mermaid = pipeline.toMermaid();
        System.out.println(mermaid);
    }
}
