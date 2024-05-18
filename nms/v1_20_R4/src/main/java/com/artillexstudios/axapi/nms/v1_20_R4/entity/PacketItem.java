package com.artillexstudios.axapi.nms.v1_20_R4.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class PacketItem extends PacketEntity implements com.artillexstudios.axapi.entity.impl.PacketItem {
    private ItemStack itemStack = new ItemStack(Material.AIR);

    public PacketItem(Location location, Consumer<com.artillexstudios.axapi.entity.impl.PacketEntity> consumer) {
        super(EntityType.ITEM, location, consumer);
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
