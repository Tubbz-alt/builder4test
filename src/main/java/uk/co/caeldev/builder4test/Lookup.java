package uk.co.caeldev.builder4test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

public class Lookup {

    private Map<String, Value> fields;

    Lookup() {
        this.fields = new HashMap<>();
    }

    void add(Value field) {
        fields.put(field.fieldId, field);
    }

    public <K> K get(String fieldName, K defaultValue) {
        Optional<Value> optionalValue = Optional.ofNullable(fields.get(fieldName));

        if (isNull(defaultValue)) {
            throw  new IllegalArgumentException("Default value cannot be null");
        }

        if (!optionalValue.isPresent()) {
            return defaultValue;
        }

        if (isNull(optionalValue.get().getValue())) {
            return defaultValue;
        }

        if (!defaultValue.getClass().equals(optionalValue.get().getValue().getClass())) {
            throw new IllegalArgumentException(String.format("value type is %s and default value type %s does not match",
                    optionalValue.get().getValue().getClass(), defaultValue.getClass()));
        }

        return isNull(optionalValue.get().getValue())? defaultValue : (K)optionalValue.get().getValue();
    }

    Map<String, Value> getFields() {
        return fields;
    }
}
