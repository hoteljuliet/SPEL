package net.hoteljuliet.spel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.*;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pipeline implements Serializable {
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

    public void execute(Context context) {
        StopWatch stopWatchPerStep = new StopWatch();
        StopWatch stopWatchOverall = new StopWatch();
        stopWatchOverall.start();
        for (Step s : steps) {

            if (BooleanUtils.isTrue(logContext)) {
                logger.debug(context.toString());
            }

            try {
                stopWatchPerStep.reset();
                stopWatchPerStep.start();
                s.execute(context);
            }
            catch(Exception ex) {
                if (BooleanUtils.isTrue(logStackTrace)) {
                    logger.error("Exception in step: " + s.getClass().getSimpleName(), ex);
                }
                if (stopOnFailure) {
                    break;
                }
            }
            finally {
                stopWatchPerStep.stop();
                s.nanos.add(stopWatchPerStep.getNanoTime());
                if (BooleanUtils.isTrue(logTiming)) {
                    logger.debug(s.getClass().getSimpleName() + " : " + stopWatchPerStep.getNanoTime() + " nanos");
                }
            }
        }

        stopWatchOverall.stop();
        if (BooleanUtils.isTrue(logTiming)) {
            logger.debug("Total : " + stopWatchOverall.getNanoTime() / 1000000 + " millis");
        }

        if (BooleanUtils.isTrue(logMetrics)) {
            // TODO
        }
    }
}
