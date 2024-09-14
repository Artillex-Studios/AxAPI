package com.artillexstudios.axapi.commands.arguments;

import com.artillexstudios.axapi.commands.StringReader;
import com.artillexstudios.axapi.commands.exception.CommandSyntaxException;

import java.util.Collection;
import java.util.Collections;

public interface ArgumentType<T> {

    T parse(StringReader var1) throws CommandSyntaxException;

    // TODO: fix
//    default <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
//        return Suggestions.empty();
//    }

    default Collection<String> getExamples() {
        return Collections.emptyList();
    }
}
