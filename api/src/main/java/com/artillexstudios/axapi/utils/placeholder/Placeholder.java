package com.artillexstudios.axapi.utils.placeholder;

import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public class Placeholder {
    private final BiFunction<Player, String, String> function;

    public Placeholder(BiFunction<Player, String, String> function) {
        this.function = function;
    }

    public String parse(Player player, String line) {
        return function.apply(player, line);
    }
}
