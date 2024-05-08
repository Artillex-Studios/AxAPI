package com.artillexstudios.axapi.items;

import org.bukkit.entity.Player;

public interface PacketItemModifierListener {

    void modifyItemStack(Player player, WrappedItemStack stack, PacketItemModifier.Context context);

    void restore(WrappedItemStack stack);
}
