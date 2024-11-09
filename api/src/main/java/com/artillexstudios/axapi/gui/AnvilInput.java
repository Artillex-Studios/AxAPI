package com.artillexstudios.axapi.gui;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.nms.NMSHandlers;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class AnvilInput {
    private static final List<AnvilInput> inputs = new ArrayList<>();
    private final Player player;
    private final WrappedItemStack stack;
    private final Component title;
    private final Consumer<InventoryClickEvent> event;
    private final Consumer<InventoryCloseEvent> closeEvent;
    private final Location location;

    public AnvilInput(Player player, WrappedItemStack stack, Component title, Consumer<InventoryClickEvent> event, Consumer<InventoryCloseEvent> closeEvent) {
        this.player = player;
        this.stack = stack;
        this.title = title;
        this.event = event;
        this.closeEvent = closeEvent;

        Location location = player.getLocation().clone();
        this.location = location.clone().add(0, 4, 0);
    }

    public static AnvilInput get(Player player) {
        AnvilInput anvilInput = null;
        for (AnvilInput input : inputs) {
            if (input.player.equals(player)) {
                anvilInput = input;
                break;
            }
        }

        return anvilInput;
    }

    public static AnvilInput remove(Player player) {
        AnvilInput anvilInput = null;
        for (AnvilInput input : inputs) {
            if (input.player.equals(player)) {
                anvilInput = input;
                break;
            }
        }

        if (anvilInput != null) {
            inputs.remove(anvilInput);
        }

        return anvilInput;
    }

    public void open() {
        NMSHandlers.getNmsHandler().openAnvilInput(this);
        inputs.add(this);
    }

    public Location location() {
        return location;
    }

    public Consumer<InventoryClickEvent> event() {
        return this.event;
    }

    public Consumer<InventoryCloseEvent> closeEvent() {
        return this.closeEvent;
    }

    public WrappedItemStack itemStack() {
        return this.stack.copy();
    }

    public Player player() {
        return this.player;
    }

    public Component title() {
        return this.title;
    }

    public static class Builder {
        private WrappedItemStack wrapped = WrappedItemStack.wrap(new ItemStack(Material.AIR));
        private Component title = Component.empty();
        private Consumer<InventoryClickEvent> event = (e) -> {
        };
        private Consumer<InventoryCloseEvent> closeEvent = (e) -> {
        };

        public Builder item(WrappedItemStack wrapped) {
            this.wrapped = wrapped;
            return this;
        }

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder event(Consumer<InventoryClickEvent> event) {
            this.event = event;
            return this;
        }

        public Builder closeEvent(Consumer<InventoryCloseEvent> closeEvent) {
            this.closeEvent = closeEvent;
            return this;
        }

        public AnvilInput build(Player player) {
            return new AnvilInput(player, this.wrapped, this.title, this.event, this.closeEvent);
        }
    }
}
