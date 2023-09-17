package net.hoteljuliet.spel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Pipeline implements Serializable {

    public static final String[] defaultPredicatePackages = {"net.hoteljuliet.spel.predicates"};
    public static final String[] defaultStatementPackages = {"net.hoteljuliet.spel.statements"};

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

    public String snapshot() {
        // TODO synchronize execute and snapshot???
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;

        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            return Base64.encodeBase64String(byteArrayOutputStream.toByteArray());
        }
        catch(Exception ex) {
            logger.error("exception in Pipeline snapshot", ex);
            throw new RuntimeException(ex);
        }
        finally {
            IOUtils.closeQuietly(byteArrayOutputStream, objectOutputStream);
        }
    }

    public static Pipeline restore(String snapshot) {
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byte[] bytes = Base64.decodeBase64(snapshot);
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (Pipeline) objectInputStream.readObject();
        }
        catch(Exception ex) {
            logger.error("exception in Pipeline snapshot", ex);
            throw new RuntimeException(ex);
        }
        finally {
            IOUtils.closeQuietly(byteArrayInputStream, objectInputStream);
        }
    }

    public List<Map<String, Object>> config;
    private List<StepBase> stepBases;
    public Boolean stopOnFailure;
    public Boolean logStackTrace;
    public Boolean logPerformance;

    public void parse() {
        Parser parser = new Parser(defaultPredicatePackages, defaultStatementPackages);
        stepBases = parser.parse(config);
    }

    public void parse(String[] predicatePackages, String[] statementPackages) {
        String[] allPredicatePackages = ArrayUtils.addAll(defaultPredicatePackages, predicatePackages);
        String[] allStatementPackages = ArrayUtils.addAll(defaultStatementPackages, statementPackages);
        Parser parser = new Parser(allPredicatePackages, allStatementPackages);
        stepBases = parser.parse(config);
    }

    public void execute() {
        execute(new Context());
    }

    // TODO: consider making this walk the entire graph, not just the top level
    public void execute(Context context) {

        // TODO: implement or use a watchdog - see Apache and Sawmill -
        // TODO: Watchdog watchdog;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (StepBase stepBase : stepBases) {
            if (BooleanUtils.isTrue(logPerformance)) {
                logger.debug("Context before " + stepBase.getName() + ": " + context);
            }
            try {
                stepBase.execute(context);
            }
            catch(Exception ex) {
                if (BooleanUtils.isTrue(logStackTrace)) {
                    logger.debug("Exception in step: " + stepBase.getName(), ex);
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
            Long pipelineTotalMillis = stopWatch.getNanoTime() / 1000000;
            logger.debug("Pipeline total : " + pipelineTotalMillis + " millis");

            for (StepBase stepBase : context.getExecutedBaseSteps()) {
                Double pct = Double.valueOf(stepBase.runTimeNanos.getMean() / stopWatch.getNanoTime()) * 100;
                String message = String.format("name=%s, avgNanos=%.2f, pct=%.2f", stepBase.getName(), stepBase.runTimeNanos.getMean(), pct);
                logger.debug(message);
            }
        }
    }

    public List<StepBase> getBaseSteps() {
        return stepBases;
    }
}
