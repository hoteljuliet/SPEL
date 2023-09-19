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
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Pipeline implements Serializable {

    public static final String[] defaultPredicatePackages = {"net.hoteljuliet.spel.predicates"};
    public static final String[] defaultStatementPackages = {"net.hoteljuliet.spel.statements"};

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
    private List<StepBase> stepBases;
    public Boolean stopOnFailure;

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
        execute(new Context(), new PipelineMetrics());
    }

    public void execute(Context context) {
        execute(context, new PipelineMetrics());
    }

    public void execute(Context context, PipelineMetrics pipelineMetrics) {

        // TODO: implement or find/use a watchdog??? - see Apache and Sawmill for implementations
        pipelineMetrics.start();
        for (StepBase stepBase : stepBases) {

            try {
                stepBase.execute(context);
            }
            catch(Exception ex) {
                if (stopOnFailure) {
                    pipelineMetrics.setFatalRootCase(ex);
                    break;
                }
            }
            finally {
                pipelineMetrics.stop();
            }
        }
    }

    public List<StepBase> getBaseSteps() {
        return stepBases;
    }
}
