package net.hoteljuliet.spel;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Transformation actions
 * @see #ENCODE
 * @see #DECODE
 * @see #ENCRYPT
 * @see #DECRYPT
 */
public enum Action {
    /**
     * encode data
     */
    ENCODE,
    /**
     * decode data
     */
    DECODE,
    /**
     * encrypt data
     */
    ENCRYPT,
    /**
     * decrypt data
     */
    DECRYPT,
    ;

    @JsonCreator
    public static Action tryParseOrDefault(String type) {
        try {
            return Action.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }
    }
}
