package net.hoteljuliet.spel;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Action {
    ENCODE,
    DECODE,
    ENCRYPT,
    DECRYPT,
    COMPRESS,
    DECOMPRESS
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
