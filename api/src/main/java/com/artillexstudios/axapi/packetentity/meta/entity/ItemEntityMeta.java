package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemEntityMeta extends EntityMeta {

    public ItemEntityMeta(Metadata metadata) {
        super(metadata);
    }

    public void itemStack(WrappedItemStack itemStack) {
        this.metadata.set(Accessors.ITEM_SLOT, itemStack);
    }

    public WrappedItemStack itemStack() {
        return this.metadata.get(Accessors.ITEM_SLOT);
    }

    @Override
    protected void defineDefaults() {
        super.defineDefaults();
        this.metadata.define(Accessors.ITEM_SLOT, WrappedItemStack.wrap(new ItemStack(Material.AIR)));
    }
}
