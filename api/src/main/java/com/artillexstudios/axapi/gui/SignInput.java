package com.artillexstudios.axapi.gui;

import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SignInput {
    private static final List<SignInput> inputs = new ArrayList<>();
    private final Player player;
    private final Component[] lines;
    private final BiConsumer<Player, Component[]> listener;
    private final Location location;

    public SignInput(Player player, Component[] lines, BiConsumer<Player, Component[]> listener) {
        this.player = player;
        this.lines = lines;
        this.listener = listener;

        Location location = player.getLocation().clone();
        this.location = location.clone().add(0, 4, 0);
    }

    public void open() {
        NMSHandlers.getNmsHandler().openSignInput(this);
        inputs.add(this);
    }

    public Location getLocation() {
        return location;
    }

    public BiConsumer<Player, Component[]> getListener() {
        return listener;
    }

    public Component[] getLines() {
        return lines;
    }

    public Player getPlayer() {
        return player;
    }

    public static class Builder {
        private Component[] lines = new Component[0];
        private BiConsumer<Player, Component[]> response = (a, b) -> {};

        public Builder setLines(Component[] lines) {
            this.lines = lines;
            return this;
        }

        public Builder setLines(List<Component> lines) {
            this.lines = lines.toArray(new Component[0]);
            return this;
        }

        public Builder setHandler(BiConsumer<Player, Component[]> response) {
            this.response = response;
            return this;
        }

        public SignInput build(Player player) {
            return new SignInput(player, lines, response);
        }
    }

    public static SignInput remove(Player player) {
        SignInput signInput = null;
        for (SignInput input : inputs) {
            if (input.player.equals(player)) {
                signInput = input;
                break;
            }
        }

        if (signInput != null) {
            inputs.remove(signInput);
        }

        return signInput;
    }
}
