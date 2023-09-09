package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.List;
import java.util.Optional;

public class Delimit extends Step {
    private String source;
    private Character delimiter;
    private Character quote;
    private List<String> dests;

    @JsonCreator
    public Delimit(@JsonProperty(value = "source", required = true) String source,
                   @JsonProperty(value = "delimiter", required = true) Character delimiter,
                   @JsonProperty(value = "quote", required = true) Character quote,
                   @JsonProperty(value = "dests", required = true) List<String> dests) {
        this.source = source;
        this.delimiter = delimiter;
        this.dests = dests;
        this.quote = quote;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        try {
            if (context.hasField(source)) {
                String value = context.getField(source);
                CSVParser parser = CSVParser.parse(value, CSVFormat.DEFAULT.withDelimiter(delimiter).withQuote(quote).withTrim());
                List<CSVRecord> records = parser.getRecords();

                for (CSVRecord record : records) {
                    for (int i = 0; i < record.size() && i < dests.size(); i++) {
                        context.addField(dests.get(i), record.get(i));
                    }
                }
                success.increment();
            } else {
                missing.increment();
            }
        } catch (Exception ex) {
            otherFailure.increment();
        }
        return COMMAND_NEITHER;
    }
}