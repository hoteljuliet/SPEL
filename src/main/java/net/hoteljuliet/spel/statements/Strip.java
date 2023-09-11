package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Strip extends StatementStep implements Serializable {
    private String source;

    @JsonCreator
    public Strip(@JsonProperty(value = "source", required = true) String source) {
        this.source = source;

    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            String value = context.getField(source);
            String stripped = value.replaceAll("\\s+","");
            context.replaceFieldValue(source, stripped);
        }
        else {
            context.missingField(name);
        }
        return Command.COMMAND_NEITHER;
    }
}

