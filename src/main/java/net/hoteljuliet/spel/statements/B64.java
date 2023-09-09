package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Step;
import net.hoteljuliet.spel.Context;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class B64 extends Step {
    private String source;
    private String dest;
    private Boolean encode;
    protected org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
    @JsonCreator
    public B64(@JsonProperty(value = "source", required = true) String source,
               @JsonProperty(value = "dest", required = true) String dest,
               @JsonProperty(value = "encode", required = true) Boolean encode) {
        this.source = source;
        this.dest = dest;
        this.encode = encode;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        try {
            if (context.hasField(source)) {
                String value = context.getField(source);
                if (encode) {
                    String encodedString = base64.encodeAsString(value.getBytes(StandardCharsets.UTF_8));
                    context.addField(dest, encodedString);
                    success.increment();
                } else {
                    String decodedValue = new String(base64.decode(value));
                    context.addField(dest, decodedValue);
                    success.increment();
                }
            }
            else {
                missing.increment();
            }
        }
        catch(Exception ex) {
            exceptionThrown.increment();
        }
        return Command.COMMAND_NEITHER;
    }
}
