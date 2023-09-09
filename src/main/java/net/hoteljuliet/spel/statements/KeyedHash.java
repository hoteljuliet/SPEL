package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class KeyedHash extends Step {
    private String source;
    private String dest;
    private Integer iterations;
    private String salt;
    private String password;

    private javax.crypto.Mac mac;
    protected org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();

    @JsonCreator
    public KeyedHash(@JsonProperty(value = "source", required = true) String source,
                     @JsonProperty(value = "dest", required = true) String dest,
                     @JsonProperty(value = "iter", required = true) Integer iterations,
                     @JsonProperty(value = "salt", required = true) String salt,
                     @JsonProperty(value = "pass", required = true) String password) {
        this.source = source;
        this.dest = dest;
        this.iterations = iterations;
        this.salt = salt;
        this.password = password;

        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterations, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
            mac = javax.crypto.Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        try {
            if (context.hasField(source)) {
                String value = context.getField(source);
                byte[] tag = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
                String b64 = base64.encodeAsString(tag);
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

