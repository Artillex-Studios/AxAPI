package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.packetentity.meta.EntityMeta;
import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class DisplayMeta extends EntityMeta {

    public DisplayMeta(Metadata metadata) {
        super(metadata);
    }

    @Override
    protected void defineDefaults() {
        super.defineDefaults();
        this.metadata.define(Accessors.ITEM_SLOT, WrappedItemStack.wrap(new ItemStack(Material.AIR)));
    }
}
