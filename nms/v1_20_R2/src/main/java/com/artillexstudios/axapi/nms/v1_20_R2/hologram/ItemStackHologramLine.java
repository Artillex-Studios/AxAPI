package com.artillexstudios.axapi.nms.v1_20_R2.hologram;

import com.artillexstudios.axapi.entity.PacketEntityFactory;
import com.artillexstudios.axapi.entity.impl.PacketItem;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ItemStackHologramLine extends com.artillexstudios.axapi.hologram.impl.ItemStackHologramLine {
    private final PacketItem packetItem;

    public ItemStackHologramLine(Location location) {
        this.packetItem = (PacketItem) PacketEntityFactory.get().spawnEntity(location, EntityType.DROPPED_ITEM);
    }

    @Override
    public void set(@NotNull ItemStack content) {
        this.packetItem.setItemStack(content);
    }

    @Override
    public void set(@NotNull ItemStack content, @NotNull Player player) {
        this.packetItem.setItemStack(content);
    }

    @NotNull
    @Override
    public ItemStack get() {
        return this.packetItem.getItemStack();
    }

    @NotNull
    @Override
    public ItemStack get(@NotNull Player player) {
        return this.packetItem.getItemStack();
    }

    @Override
    public void hide(@NotNull Player player) {
        this.packetItem.hide(player);
    }

    @Override
    public void show(@NotNull Player player) {
        this.packetItem.show(player);
    }

    @Override
    public void teleport(@NotNull Location location) {
        this.packetItem.teleport(location);
    }

    @Override
    public void remove() {
        this.packetItem.remove();
    }

    @Override
    public Set<Player> getViewers() {
        return this.packetItem.getViewers();
    }
}
