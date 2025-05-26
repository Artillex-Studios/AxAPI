package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.placeholders.exception.PlaceholderException;
import com.artillexstudios.axapi.utils.functions.ThrowingFunction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Placeholder(String placeholder, PlaceholderArguments arguments, Pattern pattern,
                          ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler) {
    private static final Pattern placeholderRegex = Pattern.compile("<([a-zA-Z0-9]+)>");

    public Placeholder(String placeholder, PlaceholderArguments arguments, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler) {
        this(placeholder, arguments, pattern(placeholder), handler);
    }

    public PlaceholderContext newContext(PlaceholderParameters parameters, Matcher matcher) {
        return new PlaceholderContext(this, parameters, matcher);
    }

    public Matcher match(String line) {
        return this.pattern.matcher(line);
    }

    private static Pattern pattern(String placeholder) {
        StringBuilder builder = new StringBuilder();
        Matcher matcher = placeholderRegex.matcher(placeholder);
        int begin = 0;

        while (matcher.find()) {
            builder.append(Pattern.quote(placeholder.substring(begin, matcher.start())));
            builder.append("(?").append(matcher.group()).append(".+?)");
            begin = matcher.end();
        }

        builder.append(Pattern.quote(placeholder.substring(begin)));

        return Pattern.compile(builder.toString());
    }
}
