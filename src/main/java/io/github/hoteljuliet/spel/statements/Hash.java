package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Step(tag = "hash")
public class Hash extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final String algo;
    private transient MessageDigest messageDigest;
    private transient Base64 base64;

    /**
     * 
     * @param in a path in the Context to a String
     * @param out a path in the Context to where a B64-formatted string of the hash will be placed
     * @param algo the hashing algorithm to use, See {@link java.security.MessageDigest}
     */
    @JsonCreator
    public Hash(@JsonProperty(value = "in", required = true) String in,
                @JsonProperty(value = "out", required = true) String out,
                @JsonProperty(value = "algo", required = true) String algo) {
        super();
        this.in = in;
        this.out = out;
        this.algo = algo;
        this.messageDigest = DigestUtils.getDigest(algo);
        this.base64 = new Base64();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (null == base64) base64 = new Base64();
        if (null == messageDigest) messageDigest = DigestUtils.getDigest(algo);

        String value = context.getField(in);
        byte[] hashed = messageDigest.digest(value.getBytes(StandardCharsets.UTF_8));
        String b64 = base64.encodeAsString(hashed);
        context.addField(out, b64);
        return EMPTY;
    }
}
