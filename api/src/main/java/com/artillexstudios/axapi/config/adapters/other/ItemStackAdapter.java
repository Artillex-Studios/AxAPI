package com.artillexstudios.axapi.config.adapters.other;

import com.artillexstudios.axapi.config.adapters.TypeAdapter;
import com.artillexstudios.axapi.config.adapters.TypeAdapterHolder;
import com.artillexstudios.axapi.items.WrappedItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.Map;

public final class ItemStackAdapter implements TypeAdapter<ItemStack, Map<String, Object>> {
    private final WrappedItemStackAdapter wrappedItemStackAdapter = new WrappedItemStackAdapter();

    @Override
    public ItemStack deserialize(TypeAdapterHolder holder, Object input, Type type) {
        return this.wrappedItemStackAdapter.deserialize(holder, input, type).toBukkit();
    }

    @Override
    public Map<String, Object> serialize(TypeAdapterHolder holder, ItemStack value, Type type) {
        return this.wrappedItemStackAdapter.serialize(holder, WrappedItemStack.wrap(value), type);
    }
}
