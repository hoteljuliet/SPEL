package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "delimit")
public class Delimit extends StepStatement implements Serializable {
    private String source;
    private Character delimiter;
    private Character quote;
    private List<String> dests;

    @JsonCreator
    public Delimit(@JsonProperty(value = "source", required = true) String source,
                   @JsonProperty(value = "delimiter", required = true) Character delimiter,
                   @JsonProperty(value = "quote", required = true) Character quote,
                   @JsonProperty(value = "dests", required = true) List<String> dests) {
        super();
        this.source = source;
        this.delimiter = delimiter;
        this.dests = dests;
        this.quote = quote;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (context.hasField(source)) {
            String value = context.getField(source);
            // TODO: rip this out and di it without a library, just pure java (its slow)
            CSVParser parser = CSVParser.parse(value, CSVFormat.DEFAULT.withDelimiter(delimiter).withQuote(quote).withTrim());
            List<CSVRecord> records = parser.getRecords();

            for (CSVRecord record : records) {
                for (int i = 0; i < record.size() && i < dests.size(); i++) {
                    context.addField(dests.get(i), record.get(i));
                }
            }
        } else {
            missingField();
        }

        return NEITHER;
    }
}