package com.artillexstudios.axapi.utils.placeholder;

import java.util.ArrayList;

// According to some testing this implementation is ~1000x faster
// than the builtin replace method. Although this doesn't work
// as good as the regular replace function
// for our use-cases this is good enough.
public class PlaceholderParser {
    private final ArrayList<StringPlaceholder> placeholders = new ArrayList<>();
    private boolean empty = true;
    private final char startChar;
    private final char endChar;

    public PlaceholderParser(char both) {
        this(both, both);
    }

    public PlaceholderParser(char start, char end) {
        this.startChar = start;
        this.endChar = end;
    }

    public void register(StringPlaceholder placeholder) {
        placeholders.add(placeholder);
        empty = false;
    }

    public String parseAll(String string, Object... objects) {
        if (empty) {
            return string;
        }

        int length = string.length();

        StringBuilder builder = new StringBuilder(string);
        int start = -1;
        StringBuilder placeholder = new StringBuilder(length);
        for (int i = 0; i < builder.length(); i++) {
            char ch = builder.charAt(i);
            if (ch == startChar || ch == endChar) {
                placeholder.append(ch);
                if (i - 1 == start || start == -1) {
                    start = i;
                } else {
                    StringPlaceholder stringPlaceholder = getPlaceholder(placeholder);
                    placeholder = new StringBuilder(length);
                    if (stringPlaceholder != null) {
                        int lt = stringPlaceholder.getKey().length();
                        String value = stringPlaceholder.getValue(objects);
                        int len = value.length();
                        builder.replace(start, start + lt, value);
                        i = len - 1;
                    }

                    start = -1;
                }
            } else if (start != -1) {
                placeholder.append(ch);
            }
        }

        return builder.toString();
    }

    private StringPlaceholder getPlaceholder(CharSequence string) {
        for (StringPlaceholder placeholder : placeholders) {
            if (placeholder.getKey().contentEquals(string)) {
                return placeholder;
            }
        }

        return null;
    }
}
