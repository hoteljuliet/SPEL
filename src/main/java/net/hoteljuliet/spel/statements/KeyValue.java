package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Splitter;
import net.hoteljuliet.spel.Context;

import java.util.Map;
import java.util.Optional;

public class KeyValue extends StatementStep {

    private String source;
    private Character delimiter;

    private Character separator;
    private String dest;

    @JsonCreator
    public KeyValue(@JsonProperty(value = "source", required = true) String source,
                    @JsonProperty(value = "delimiter", required = true) Character delimiter,
                    @JsonProperty(value = "separator", required = true) Character separator,
                    @JsonProperty(value = "dest", required = true) String dest) {
        this.source = source;
        this.delimiter = delimiter;
        this.dest = dest;
        this.separator = separator;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            String value = context.getField(source);

            Map<String, String> result = Splitter.on(delimiter)
                    .trimResults()
                    .withKeyValueSeparator(Splitter.on(separator).trimResults())
                    .split(value);

            context.addField(dest, result);
        }
        else {
            context.missingField(name);
        }

        return COMMAND_NEITHER;
    }
}
