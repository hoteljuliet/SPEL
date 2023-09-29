package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Step(tag = "keyed-hash")
public class KeyedHash extends StepStatement implements Serializable {
    private final String in;
    private final String out;
    private final Integer iterations;
    private final String salt;
    private final String password;
    private transient Mac mac;
    private transient Base64 base64;

    /**
     * A keyed hash, performed with PBKDF2WithHmacSHA256
     * @param in a path to a String to hash.
     * @param out a path in the Context to where a B64-formatted string of the hash will be placed
     * @param iterations the number of iterations, See {@link javax.crypto.spec.PBEKeySpec}
     * @param salt the salt, See {@link javax.crypto.spec.PBEKeySpec}
     * @param password the password, See {@link javax.crypto.spec.PBEKeySpec}. Note that ${X} will be replaced with the Environment variable X.
     */
    @JsonCreator
    public KeyedHash(@JsonProperty(value = "in", required = true) String in,
                     @JsonProperty(value = "out", required = true) String out,
                     @JsonProperty(value = "iter", required = true) Integer iterations,
                     @JsonProperty(value = "salt", required = true) String salt,
                     @JsonProperty(value = "pass", required = true) String password) {
        super();
        this.in = in;
        this.out = out;
        this.iterations = iterations;
        this.salt = salt;
        this.password = password;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (null == base64) base64 = new Base64();
        if (null == mac) {
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
        String fieldValue = context.getField(in);
        byte[] tag = mac.doFinal(fieldValue.getBytes(StandardCharsets.UTF_8));
        String b64 = base64.encodeAsString(tag);
        context.addField(out, b64);
        return EMPTY;
    }
}

