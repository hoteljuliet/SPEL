package io.github.hoteljuliet.spel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.MoreObjects;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;

import java.util.List;
import java.util.Map;

public enum FieldType {
    INT {
        @Override
        public Object convertFrom(Object value) {
            return Longs.tryParse(String.valueOf(value)).intValue();
        }
        @Override
        public Boolean isInstance(Object value) {
            return value instanceof Integer;
        }
    },
    LONG {
        @Override
        public Object convertFrom(Object value) {
            return Longs.tryParse(String.valueOf(value));
        }

        @Override
        public Boolean isInstance(Object value) {
            return value instanceof Long;
        }
    },
    FLOAT {
        @Override
        public Object convertFrom(Object value) {
            return Doubles.tryParse(String.valueOf(value)).floatValue();
        }

        @Override
        public Boolean isInstance(Object value) {
            return value instanceof Float;
        }
    },
    DOUBLE {
        @Override
        public Object convertFrom(Object value) { return Doubles.tryParse(String.valueOf(value)); }

        @Override
        public Boolean isInstance(Object value) {
            return value instanceof Double;
        }
    },
    STRING {
        @Override
        public Object convertFrom(Object value) {
            return String.valueOf(value);
        }

        @Override
        public Boolean isInstance(Object value) {
            return value instanceof String;
        }
    },
    BOOLEAN {
        @Override
        public Object convertFrom(Object value) { return String.valueOf(value).matches("^(t|true|yes|y|1)$"); }

        @Override
        public Boolean isInstance(Object value) { return value instanceof Boolean; }
    },
    LIST {
        @Override
        public Object convertFrom(Object value) { return (List) value; }

        @Override
        public Boolean isInstance(Object value) { return value instanceof List; }

    },
    MAP {
        @Override
        public Object convertFrom(Object value) { return (Map) value; }

        @Override
        public Boolean isInstance(Object value) { return value instanceof Map; }
    }
    ;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    @JsonCreator
    public static FieldType tryParseOrDefault(String type) {
        try {
            return FieldType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    public abstract Object convertFrom(Object value);

    public abstract Boolean isInstance(Object value);

    public Object convertFrom(Object value, Object defaultValue) {
        return MoreObjects.firstNonNull(convertFrom(value), defaultValue);
    }
}
