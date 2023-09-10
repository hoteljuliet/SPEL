package net.hoteljuliet.spel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Pipeline {

    public List<Step> getSteps() {
        return steps;
    }

    private static final Logger logger = LoggerFactory.getLogger(Pipeline.class);

    private static final ObjectMapper objectMapper =
            new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);
    public static Pipeline fromFile(String path) throws IOException {
        File file = new File(path);
        FileReader fileReader = new FileReader(file);
        return objectMapper.readValue(fileReader, Pipeline.class);
    }

    public static Pipeline fromString(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, Pipeline.class);
    }

    public static Pipeline fromResource(String path) throws IOException {
        return objectMapper.readValue(Pipeline.class.getResourceAsStream(path), Pipeline.class);
    }
    public List<Map<String, Object>> config;
    private List<Step> steps;
    public Boolean stopOnFailure;
    public Boolean logStackTrace;
    public Boolean logPerformance;

    public void build() {
        steps = Parser.build(config);
    }

    public void execute() {
        execute(new Context());
    }

    // TODO: consider making this walk the entire graph, not just the top level
    //
    public void execute(Context context) {

        context.initializeMetrics(steps);

        // TODO: implement or use a watchdog - see Apache and Sawmill -
        // TODO: Watchdog watchdog;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (Step step : steps) {
            if (BooleanUtils.isTrue(logPerformance)) {
                logger.debug("Context before " + step.getName() + ": " + context.toString());
            }

            try {
                step.execute(context);
            }
            catch(Exception ex) {
                if (BooleanUtils.isTrue(logStackTrace)) {
                    logger.debug("Exception in step: " + step.getName(), ex);
                }
                if (stopOnFailure) {
                    break;
                }
            }
            finally {
                ;
            }
        }

        stopWatch.stop();

        if (BooleanUtils.isTrue(logPerformance)) {
            logger.debug("----------pipeline performance-----------------");
            Long pipelineTotalNanos = stopWatch.getNanoTime();
            Long pipelineTotalMillis = stopWatch.getNanoTime() / 1000000;
            logger.debug("Total : " + pipelineTotalMillis + " millis");

            for (Map.Entry<String, StepMetrics> entry : context.metricsPerStep.entrySet()) {
                double pct = (entry.getValue().runTimeNanos.getMean() / pipelineTotalNanos) * 100;
                String message = entry.getKey() + ": " + entry.getValue() + ",pct=" + String.format("%,.2f", pct);
                logger.debug(message);
            }
        }
    }
}
