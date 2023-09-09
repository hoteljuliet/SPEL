package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;
public class Hash extends StatementStep {
    private String source;
    private String dest;
    private MessageDigest messageDigest;
    protected org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();
    @JsonCreator
    public Hash(@JsonProperty(value = "source", required = true) String source,
                @JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "algo", required = true) String algo) {
        this.source = source;
        this.dest = dest;
        this.messageDigest = DigestUtils.getDigest(algo);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (context.hasField(source)) {
            String value = context.getField(source);
            byte[] hashed = messageDigest.digest(value.getBytes(StandardCharsets.UTF_8));
            String b64 = base64.encodeAsString(hashed);
            context.addField(dest, b64);
        }
        else {
            context.missingField(name);
        }
        return COMMAND_NEITHER;
    }
}
