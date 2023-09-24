package com.artillexstudios.axapi.entity.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PacketItem extends PacketEntity {

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);

    void setItemStack(ItemStack itemStack, Player player);

    ItemStack getItemStack(Player player);
}
