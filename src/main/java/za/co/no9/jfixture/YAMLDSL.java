package za.co.no9.jfixture;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;

public class YAMLDSL {
    private final Object yaml;

    public YAMLDSL(Object yaml) {
        this.yaml = yaml;
    }

    public static YAMLDSL fromYAML(Object yaml) {
        return new YAMLDSL(yaml);
    }

    public boolean isMap() {
        return yaml instanceof Map;
    }

    public YAMLMap map() {
        return mapElseException("An attempt was made to use " + String.valueOf(yaml) + " as a map.");
    }

    public YAMLMap mapElseException(String exceptionMessage) {
        if (yaml instanceof Map) {
            return new YAMLMap((Map) yaml);
        } else {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    public Iterable iterableElseException(String exceptionMessage) {
        if (yaml instanceof Iterable) {
            return (Iterable) yaml;
        } else {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    static public class YAMLMap {
        private Map map;

        public YAMLMap(Map map) {
            this.map = map;
        }

        public Field field(String name) {
            return new Field(map.get(name));
        }

        public Set<String> keySet() {
            return map.keySet();
        }

        public Object get(String key) {
            return map.get(key);
        }
    }

    static public class Field {
        private Object value;
        private String ifBlankExceptionMessage = null;
        private String ifNullException = null;
        private Object defaultValue;

        public Field(Object value) {
            this.value = value;
        }

        public String asString() {
            checkField();
            return String.valueOf(value());
        }

        public YAMLMap map() {
            checkField();

            if (value instanceof Map) {
                return new YAMLMap((Map) value);
            }
            throw new IllegalArgumentException("An attempt was made to use " + String.valueOf(value) + " as a map.");
        }

        public Field ifBlankException(String message) {
            ifBlankExceptionMessage = message;
            return this;
        }

        public Field ifNullException(String message) {
            ifNullException = message;
            return this;
        }

        public Field ifBlankDefault(Object defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Iterable<Object> iterableElseException(String exceptionMessage) {
            checkField();

            if (value instanceof Iterable) {
                return (Iterable) value;
            }
            throw new IllegalArgumentException(exceptionMessage);
        }

        public BooleanField ifEmptyDefault(boolean defaultValue) {
            return new BooleanField(this).defaultVault(defaultValue);
        }

        protected void checkField() {
            if (ifNullException != null && value == null) {
                throw new IllegalArgumentException(ifNullException);
            }
            if (ifBlankExceptionMessage != null && StringUtils.isBlank(value == null ? null : value.toString())) {
                throw new IllegalArgumentException(ifBlankExceptionMessage);
            }
        }

        protected Object value() {
            return defaultValue != null && value == null ? defaultValue : value;
        }
    }

    static public class BooleanField {
        private final Field field;

        BooleanField(Field field) {
            this.field = field;
        }

        public BooleanField defaultVault(boolean defaultValue) {
            field.defaultValue = defaultValue;
            return this;
        }

        public boolean asBoolean() {
            field.checkField();

            Object value = field.value();

            if (value instanceof Boolean) {
                return (Boolean) value;
            } else {
                throw new IllegalArgumentException("Attempt to use " + String.valueOf(value) + " as a boolean.");
            }
        }
    }
}
