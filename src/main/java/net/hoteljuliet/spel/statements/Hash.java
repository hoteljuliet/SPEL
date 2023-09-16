package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

@Step(tag = "hash")
public class Hash extends StatementBaseStep implements Serializable {
    private String source;
    private String dest;

    private String algo;
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

        if (context.hasField(source)) {
            String value = context.getField(source);
            byte[] hashed = messageDigest.digest(value.getBytes(StandardCharsets.UTF_8));
            String b64 = base64.encodeAsString(hashed);
            context.addField(dest, b64);
        }
        else {
            missingField.increment();
        }
        return NEITHER;
    }

    @Override
    public void restore() {
        super.restore();
        this.messageDigest = DigestUtils.getDigest(algo);
        this.base64 = new Base64();
    }
}
