package com.artillexstudios.axapi.gui.inventory.modifier;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.items.component.DataComponents;
import com.artillexstudios.axapi.items.nbt.CompoundTag;

public enum DefaultWrappedItemStackModifier implements WrappedItemStackModifier {
    INSTANCE;

    @Override
    public WrappedItemStack modify(WrappedItemStack stack) {
        CompoundTag tag = stack.get(DataComponents.customData());
        tag.putBoolean("axapi-gui", true);
        stack.set(DataComponents.customData(), tag);
        return stack;
    }
}
