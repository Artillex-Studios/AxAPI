package com.artillexstudios.axapi.gui.configuration.actions;

import com.artillexstudios.axapi.context.HashMapContext;
import org.bukkit.entity.Player;

public abstract class Action<T> {
    private final T value;

    public Action(String data) {
        this.value = this.transform(data);
    }

    public abstract T transform(String data);

    public abstract void run(Player player, HashMapContext context);

    public T value() {
        return this.value;
    }
}
