package com.artillexstudios.axapi.utils.placeholder;

import java.util.function.Function;

public class StringPlaceholder {
    private final String key;
    private final Function<Object[], String> function;

    public StringPlaceholder(String key, Function<Object[], String> function) {
        this.key = key;
        this.function = function;
    }

    public String getKey() {
        return key;
    }

    public String getValue(Object[] objects) {
        return function.apply(objects);
    }
}
