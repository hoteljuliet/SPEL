package net.hoteljuliet.spel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.exec.Watchdog;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.*;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public Boolean logMetrics;
    public Boolean logTiming;
    public Boolean logContext;

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

        Watchdog watchdog;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (Step step : steps) {
            if (BooleanUtils.isTrue(logContext)) {
                logger.debug(context.toString());
            }

            try {
                step.execute(context);
            }
            catch(Exception ex) {
                if (BooleanUtils.isTrue(logStackTrace)) {
                    logger.error("Exception in step: " + step.getName(), ex);
                }
                if (stopOnFailure) {
                    break;
                }
            }
            finally {
                if (BooleanUtils.isTrue(logTiming)) {
                    logger.debug(step.getName() + " : " + context.getMetrics(step.getName()).lastRunNanos + " nanos");
                }
            }
        }

        stopWatch.stop();
        if (BooleanUtils.isTrue(logTiming)) {
            logger.debug("Total : " + stopWatch.getNanoTime() / 1000000 + " millis");
        }

        if (BooleanUtils.isTrue(logMetrics)) {
            for (Map.Entry<String, StepMetrics> entry : context.metricsPerStep.entrySet()) {
                logger.debug("Metrics for " + entry.getKey() + " : " + entry.getValue());
            }
        }
    }
}
