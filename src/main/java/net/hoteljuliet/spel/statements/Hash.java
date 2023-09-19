package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Step(tag = "hash")
public class Hash extends StepStatement implements Serializable {
    private final String source;
    private final String dest;
    private final String algo;
    private transient MessageDigest messageDigest;
    private transient Base64 base64;

    @JsonCreator
    public Hash(@JsonProperty(value = "source", required = true) String source,
                @JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "algo", required = true) String algo) {
        super();
        this.source = source;
        this.dest = dest;
        this.algo = algo;
        this.messageDigest = DigestUtils.getDigest(algo);
        this.base64 = new Base64();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (null == base64) base64 = new Base64();
        if (null == messageDigest) messageDigest = DigestUtils.getDigest(algo);

        String value = context.getField(source);
        byte[] hashed = messageDigest.digest(value.getBytes(StandardCharsets.UTF_8));
        String b64 = base64.encodeAsString(hashed);
        context.addField(dest, b64);
        return EMPTY;
    }
}
