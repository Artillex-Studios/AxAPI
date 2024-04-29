package com.artillexstudios.axapi.items;

import com.artillexstudios.axapi.items.component.DataComponent;
import com.artillexstudios.axapi.nms.NMSHandlers;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

public interface WrappedItemStack {

    static WrappedItemStack wrap(ItemStack itemStack) {
        return NMSHandlers.getNmsHandler().wrapItem(itemStack);
    }

    static WrappedItemStack wrap(byte[] bytes) {
        return NMSHandlers.getNmsHandler().wrapItem(bytes);
    }

    static WrappedItemStack wrap(String snbt) {
        return NMSHandlers.getNmsHandler().wrapItem(snbt);
    }

    static <T> T edit(ItemStack itemStack, Function<WrappedItemStack, T> function) {
        WrappedItemStack wrapped = wrap(itemStack);
        T result = function.apply(wrapped);
        wrapped.finishEdit();
        return result;
    }

    <T> void set(DataComponent<T> component, T value);

    <T> T get(DataComponent<T> component);

    void setAmount(int amount);

    int getAmount();

    ItemStack toBukkit();

    String toSNBT();

    byte[] serialize();

    void finishEdit();
}
