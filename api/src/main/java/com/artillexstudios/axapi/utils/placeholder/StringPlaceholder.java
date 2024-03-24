package com.artillexstudios.axapi.utils.placeholder;

public class StringPlaceholder {
    private final String key;
    private final String value;

    public StringPlaceholder(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
