package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Step(tag = "delimit")
public class Delimit extends StepStatement implements Serializable {
    private String in;
    private Character delimiter;
    private Character quote;
    private List<String> out;

    @JsonCreator
    public Delimit(@JsonProperty(value = "in", required = true) String in,
                   @JsonProperty(value = "delimiter", required = true) Character delimiter,
                   @JsonProperty(value = "quote", required = true) Character quote,
                   @JsonProperty(value = "out", required = true) List<String> out) {
        super();
        this.in = in;
        this.delimiter = delimiter;
        this.out = out;
        this.quote = quote;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        String value = context.getField(in);
        // TODO: rip this out and do it without a library, just pure java
        CSVParser parser = CSVParser.parse(value, CSVFormat.DEFAULT.withDelimiter(delimiter).withQuote(quote).withTrim());
        List<CSVRecord> records = parser.getRecords();

        for (CSVRecord record : records) {
            for (int i = 0; i < record.size() && i < out.size(); i++) {
                context.addField(out.get(i), record.get(i));
            }
        }
        return EMPTY;
    }
}