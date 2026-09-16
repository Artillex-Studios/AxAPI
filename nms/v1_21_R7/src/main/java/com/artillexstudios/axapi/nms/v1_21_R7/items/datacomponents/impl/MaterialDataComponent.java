package com.artillexstudios.axapi.nms.v1_21_R7.items.datacomponents.impl;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.components.DataComponent;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public record MaterialDataComponent() implements DataComponent<Material> {

    @Override
    public void apply(WrappedItemStack stack, Material data) {
        ItemStack wrapped = ((com.artillexstudios.axapi.nms.v1_21_R7.items.WrappedItemStack) stack).itemStack;
        wrapped.setItem(CraftMagicNumbers.getItem(data));
    }

    @Override
    public Material getData(WrappedItemStack stack) {
        ItemStack wrapped = ((com.artillexstudios.axapi.nms.v1_21_R7.items.WrappedItemStack) stack).itemStack;
        return CraftMagicNumbers.getMaterial(wrapped.getItem());
    }

    @Override
    public String getId() {
        return "material";
    }
}
