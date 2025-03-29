package com.artillexstudios.axapi.packetentity.meta.entity;

import com.artillexstudios.axapi.packetentity.meta.Metadata;
import com.artillexstudios.axapi.packetentity.meta.serializer.Accessors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemDisplayMeta extends DisplayMeta {

    public ItemDisplayMeta(Metadata metadata) {
        super(metadata);
    }

    public void displayedItem(ItemStack wrappedItemStack) {
        this.metadata.set(Accessors.DISPLAYED_ITEM, wrappedItemStack);
    }

    public ItemStack displayedItem() {
        return this.metadata.get(Accessors.DISPLAYED_ITEM);
    }

    public void displayType(DisplayType type) {
        this.metadata.set(Accessors.DISPLAY_TYPE, (byte) type.ordinal());
    }

    public DisplayType displayType() {
        return DisplayType.values()[this.metadata.get(Accessors.DISPLAY_TYPE)];
    }

    @Override
    protected void defineDefaults() {
        this.metadata.define(Accessors.DISPLAYED_ITEM, new ItemStack(Material.AIR));
        this.metadata.define(Accessors.DISPLAY_TYPE, (byte) 0);
    }

    public enum DisplayType {
        NONE,
        THIRD_PERSON_LEFT_HAND,
        THIRD_PERSON_RIGHT_HAND,
        FIRST_PERSON_LEFT_HAND,
        FIRST_PERSON_RIGHT_HAND,
        HEAD,
        GUI,
        GROUND,
        FIXED;
    }
}
