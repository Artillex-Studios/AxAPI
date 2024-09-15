package com.artillexstudios.axapi.commands.arguments;

import com.artillexstudios.axapi.commands.CommandContext;
import com.artillexstudios.axapi.commands.StringReader;
import com.artillexstudios.axapi.commands.exception.CommandSyntaxException;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public interface ArgumentType<T> {

    T parse(StringReader reader) throws CommandSyntaxException;

    Class<T> type();

    default Stream<String> listSuggestions(CommandContext context) {
        return Stream.empty();
    }

    default Collection<String> getExamples() {
        return Collections.emptyList();
    }
}
