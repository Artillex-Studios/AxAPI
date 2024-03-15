package com.artillexstudios.axapi.items;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PacketItemModifierListener {

    void modifyItemStack(Player player, WrappedItemStack stack);
}
