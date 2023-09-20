package net.hoteljuliet.spel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

public class Pipeline implements Serializable {

    public static final String[] defaultPredicatePackages = {"net.hoteljuliet.spel.predicates"};
    public static final String[] defaultStatementPackages = {"net.hoteljuliet.spel.statements"};

    private static final ObjectMapper objectMapper =
            new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);

    // fields from yaml
    public List<Map<String, Object>> config;
    private List<StepBase> stepBases;
    public Boolean stopOnFailure;

    // fields that are serialized/deserialized
    private StopWatch stopWatch;
    private SummaryStatistics runTimeNanos;
    private LimitedCountingMap stackTraces;
    private LongAdder totalFailures;

    public void init() {
        if (null == stopWatch) stopWatch = new StopWatch();
        if (null == runTimeNanos) runTimeNanos = new SummaryStatistics();
        if (null == stackTraces) stackTraces = new LimitedCountingMap();
        if (null == totalFailures) totalFailures = new LongAdder();
    }

    public static Pipeline fromFile(String path) {
        File file = new File(path);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            Pipeline p = objectMapper.readValue(fileReader, Pipeline.class);
            p.init();
            return p;

        } catch (IOException ex) {
           return null;
        }
    }

    public static Pipeline fromString(String value) {
        try {
            Pipeline p = objectMapper.readValue(value, Pipeline.class);
            p.init();
            return p;
        }
        catch (JsonProcessingException ex) {
            return null;
        }
    }

    public static Pipeline fromResource(String path) {

        try {
            Pipeline p = objectMapper.readValue(Pipeline.class.getResourceAsStream(path), Pipeline.class);
            p.init();
            return p;
        }
        catch (IOException ex) {
            return null;
        }
    }

    public Integer parse() {
        Parser parser = new Parser(defaultPredicatePackages, defaultStatementPackages);
        stepBases = parser.parse(config);
        return parser.getFactory().getInstanceCounter().intValue();
    }

    public Integer parse(String[] predicatePackages, String[] statementPackages) {
        String[] allPredicatePackages = ArrayUtils.addAll(defaultPredicatePackages, predicatePackages);
        String[] allStatementPackages = ArrayUtils.addAll(defaultStatementPackages, statementPackages);
        Parser parser = new Parser(allPredicatePackages, allStatementPackages);
        stepBases = parser.parse(config);
        return parser.getFactory().getInstanceCounter().intValue();
    }

    public void execute() {
        execute(new Context());
    }

    public void execute(Context context) {
        // TODO: implement or find/use a watchdog??? - see Apache and Sawmill for implementations
        stopWatch.reset();
        stopWatch.start();
        for (StepBase stepBase : stepBases) {
            try {
                stepBase.execute(context);
            }
            catch(Exception ex) {
                totalFailures.increment();
                List<String> stackTrace = ExceptionUtils.getRootCauseStackTraceList(ex);
                String trace = StringUtils.join(stackTrace, ',');
                stackTraces.put(trace);

                if (stopOnFailure) {
                    break;
                }
            }
            finally {

            }
        }
        stopWatch.stop();
        runTimeNanos.addValue(stopWatch.getNanoTime());
    }

    public List<StepBase> getBaseSteps() {
        return stepBases;
    }

    public SummaryStatistics getRunTimeNanos() {
        return runTimeNanos;
    }

    public LimitedCountingMap getStackTraces() {
        return stackTraces;
    }

    public LongAdder getTotalFailures() {
        return totalFailures;
    }
}
