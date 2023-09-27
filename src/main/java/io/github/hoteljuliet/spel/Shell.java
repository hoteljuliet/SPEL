package io.github.hoteljuliet.spel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Map;
import java.util.Scanner;

public class Shell {

    private static final ObjectMapper objectMapper =
            new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);

    public static void main(String[] args) {
        Scanner reader = null;
        Context context = new Context(new Pipeline());
        Parser parser = new Parser();
        try {
            do {
                try {
                    reader = new Scanner(System.in);
                    System.out.print("spel$ ");
                    String userInput = reader.nextLine();

                    if (userInput.isEmpty()) {
                        continue;
                    }
                    else if (userInput.equalsIgnoreCase("show")) {
                        System.out.println("-> " + context);
                    }
                    else if (userInput.equalsIgnoreCase("clear")) {
                        context.clear();
                        System.out.println("-> " + context);
                    }
                    else if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("quit")) {
                        break;
                    }
                    else {
                        Map<String, Object> node = objectMapper.readValue(userInput, Map.class);
                        StepBase stepBase = parser.parse(node);
                        stepBase.execute(context);
                        System.out.println("-> " + context);
                        System.out.println("-> Avg Time: " + stepBase.runTimeNanos.getMean() + " nanos");
                    }
                }
                catch(Exception ex) {
                    String rootCase = ExceptionUtils.getRootCauseMessage(ex);
                    System.out.println(rootCase);
                }
            }
            while (true);
        }
        catch(Exception ex) {
            String rootCase = ExceptionUtils.getRootCauseMessage(ex);
            System.out.println(rootCase);
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
    }
}
