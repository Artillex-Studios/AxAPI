package com.artillexstudios.axapi.utils.placeholder;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderParser {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%.+%");
    private final ArrayList<StringPlaceholder> placeholders = new ArrayList<>();

    public void register(StringPlaceholder placeholder) {
        placeholders.add(placeholder);
    }

    public String parseAll(String string) {
        if (placeholders.isEmpty()) {
            return string;
        }

        StringBuilder builder = new StringBuilder(string);
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(string);
        while (matcher.find()) {
            String matched = matcher.group();

            StringPlaceholder placeholder = getPlaceholder(matched);
            if (placeholder != null) {
                int start = builder.indexOf(placeholder.getKey());
                int length = placeholder.getKey().length();
                builder.replace(start, start + length, placeholder.getValue());
            }
        }

        return builder.toString();
    }

    private StringPlaceholder getPlaceholder(String string) {
        for (StringPlaceholder placeholder : placeholders) {
             if (placeholder.getKey().equalsIgnoreCase(string)) {
                 return placeholder;
             }
        }
        
        return null;
    }
}
