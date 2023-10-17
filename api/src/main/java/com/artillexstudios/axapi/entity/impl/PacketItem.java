package com.artillexstudios.axapi.entity.impl;

import org.bukkit.inventory.ItemStack;

public interface PacketItem extends PacketEntity {

    ItemStack getItemStack();

    void setItemStack(ItemStack itemStack);
}
