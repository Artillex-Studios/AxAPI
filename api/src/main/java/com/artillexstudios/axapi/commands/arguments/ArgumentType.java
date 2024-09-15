package com.artillexstudios.axapi.commands.arguments;

import com.artillexstudios.axapi.commands.CommandContext;
import com.artillexstudios.axapi.commands.exception.CommandSyntaxException;

import java.util.stream.Stream;

public interface ArgumentType<T, Z> {

    T parse(Object from) throws CommandSyntaxException;

    Class<T> type();

    ArgumentType<?, ?> internalType();

    default Stream<String> listSuggestions(CommandContext context) {
        return Stream.empty();
    }
}
