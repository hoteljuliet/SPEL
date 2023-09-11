package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.SpelUtils;
import org.apache.commons.codec.binary.Base64;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class B64 extends StatementStep implements Serializable {
    private String source;
    private String dest;
    private Boolean encode;
    private transient Base64 base64;
    @JsonCreator
    public B64(@JsonProperty(value = "source", required = true) String source,
               @JsonProperty(value = "dest", required = true) String dest,
               @JsonProperty(value = "encode", required = true) Boolean encode) {
        this.source = source;
        this.dest = dest;
        this.encode = encode;
        this.base64 = new Base64();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            String value = context.getField(source);
            if (encode) {
                String encodedString = base64.encodeAsString(value.getBytes(StandardCharsets.UTF_8));
                context.addField(dest, encodedString);
            } else {
                String decodedValue = new String(base64.decode(value));
                context.addField(dest, decodedValue);
            }
        }
        else {
            context.missingField(name);
        }
        return Command.COMMAND_NEITHER;
    }

    @Override
    public void restore() {
        super.restore();
        this.base64 = new Base64();
    }
}
