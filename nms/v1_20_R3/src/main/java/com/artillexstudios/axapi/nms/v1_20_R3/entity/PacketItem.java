package com.artillexstudios.axapi.nms.v1_20_R3.entity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class PacketItem extends PacketEntity implements com.artillexstudios.axapi.entity.impl.PacketItem {
    private ItemStack itemStack;

    public PacketItem(Location location, Consumer<com.artillexstudios.axapi.entity.impl.PacketEntity> consumer) {
        super(EntityType.DROPPED_ITEM, location, consumer);
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        data.set(EntityData.ITEM_ITEM_STACK, CraftItemStack.asNMSCopy(itemStack));
    }

    @Override
    public void defineEntityData() {
        super.defineEntityData();

        data.define(EntityData.ITEM_ITEM_STACK, net.minecraft.world.item.ItemStack.EMPTY);
    }
}
