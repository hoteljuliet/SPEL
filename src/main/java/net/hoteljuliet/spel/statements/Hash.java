package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;
public class Hash extends Step {
    private String source;
    private String dest;
    private MessageDigest messageDigest;
    @JsonCreator
    public Hash(@JsonProperty(value = "source", required = true) String source,
                @JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "algo", required = true) String algo) {
        this.source = source;
        this.dest = dest;
        this.messageDigest = DigestUtils.getDigest(algo);
    }

    @Override
    public Optional<Boolean> execute(Context context) throws Exception {
        try {
            if (context.hasField(source)) {
                String value = context.getField(source);
                byte[] hashed = messageDigest.digest(value.getBytes(StandardCharsets.UTF_8));
                String b64 = base64.encodeAsString(hashed);
                context.addField(dest, b64);
                success.increment();
            }
            else {
                missing.increment();
            }
        }
        catch(Exception ex) {
            exceptionThrown.increment();
        }
        return COMMAND_NEITHER;
    }
}
