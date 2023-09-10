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

        // TODO: re-implements, this is taking 10 ms!
        //  - add-i: {dest: kvString, value: 'this=that, you=me, potatoe=potatoh, tomatoe=tomahtoh'}
        //  - key-value: {source: kvString, delimiter: ',', separator: '=',  dest: kvMap}
        //  key-value2 : 10997831 nanos

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
