package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepStatement;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Step(tag = "hash")
public class Hash extends StepStatement implements Serializable {
    private final String value;
    private final String dest;
    private final String algo;
    private transient MessageDigest messageDigest;
    private transient Base64 base64;

    @JsonCreator
    public Hash(@JsonProperty(value = "value", required = true) String value,
                @JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "algo", required = true) String algo) {
        super();
        this.value = value;
        this.dest = dest;
        this.algo = algo;
        this.messageDigest = DigestUtils.getDigest(algo);
        this.base64 = new Base64();
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (null == base64) base64 = new Base64();
        if (null == messageDigest) messageDigest = DigestUtils.getDigest(algo);

        String v = context.getField(value);
        byte[] hashed = messageDigest.digest(v.getBytes(StandardCharsets.UTF_8));
        String b64 = base64.encodeAsString(hashed);
        context.addField(dest, b64);
        return EMPTY;
    }
}
