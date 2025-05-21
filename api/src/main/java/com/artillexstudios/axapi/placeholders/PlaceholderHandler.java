package com.artillexstudios.axapi.placeholders;

import com.artillexstudios.axapi.placeholders.exception.PlaceholderException;
import com.artillexstudios.axapi.utils.functions.ThrowingFunction;

import java.util.concurrent.ConcurrentLinkedDeque;

public class PlaceholderHandler {
    private static final ConcurrentLinkedDeque<Placeholder> placeholders = new ConcurrentLinkedDeque<>();

    void test() {
        register("axkoth_wins_<koth>", new PlaceholderArguments(new PlaceholderArgument<>("koth", null)), ctx -> {
            Object koth = ctx.argument("koth");
            return koth.toString();
        });
    }

    public static void register(String placeholder, PlaceholderArguments arguments, ThrowingFunction<PlaceholderContext, String, PlaceholderException> handler) {

    }
}
