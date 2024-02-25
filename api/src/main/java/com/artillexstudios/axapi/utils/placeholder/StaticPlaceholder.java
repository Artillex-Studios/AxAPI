package com.artillexstudios.axapi.utils.placeholder;

import org.bukkit.entity.Player;

import java.util.function.Function;

public class StaticPlaceholder extends Placeholder {

    public StaticPlaceholder(Function<String, String> function) {
        super((player, string) -> {
            return function.apply(string);
        });
    }

    @Override
    public String parse(Player player, String line) {
        return super.parse(player, line);
    }
}
