package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.placeholders.exception.PlaceholderException;
import com.artillexstudios.axapi.utils.functions.ThrowingFunction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Placeholder(String placeholder, PlaceholderArguments arguments, Pattern pattern, boolean placeholderAPI,
                          ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler,
                          ThreadLocal<Matcher> matchers) {
    private static final Pattern placeholderRegex = Pattern.compile("<([a-zA-Z0-9]+)>");

    public Placeholder(String placeholder, PlaceholderArguments arguments, boolean placeholderAPI, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler) {
        this(placeholder, arguments, pattern(placeholder), placeholderAPI, handler, ThreadLocal.withInitial(() -> pattern(placeholder).matcher("")));
    }

    public PlaceholderContext newContext(PlaceholderParameters parameters, Matcher matcher) {
        return new PlaceholderContext(this, parameters, matcher);
    }

    public Matcher match(String line) {
        return this.matchers.get().reset(line);
    }

    private static Pattern pattern(String placeholder) {
        StringBuilder builder = new StringBuilder();
        Matcher matcher = placeholderRegex.matcher(placeholder);
        int begin = 0;

        while (matcher.find()) {
            builder.append(Pattern.quote(placeholder.substring(begin, matcher.start()))).append("++");
            builder.append("(?").append(matcher.group()).append(".+?)");
            begin = matcher.end();
        }

        builder.append(Pattern.quote(placeholder.substring(begin)));

        return Pattern.compile(builder.toString());
    }
}
