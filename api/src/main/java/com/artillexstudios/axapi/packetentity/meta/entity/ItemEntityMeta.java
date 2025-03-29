package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemEntityMeta extends EntityMeta {

    public ItemEntityMeta(Metadata metadata) {
        super(metadata);
    }

    public void itemStack(ItemStack itemStack) {
        this.metadata.set(Accessors.ITEM_SLOT, itemStack);
    }

    public ItemStack itemStack() {
        return this.metadata.get(Accessors.ITEM_SLOT);
    }

    @Override
    protected void defineDefaults() {
        this.metadata.define(Accessors.ITEM_SLOT, new ItemStack(Material.AIR));
    }
}
