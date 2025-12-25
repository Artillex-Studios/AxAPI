package com.artillexstudios.axapi.nms.v1_21_R7_paper.items.datacomponents.impl;

import com.artillexstudios.axapi.items.WrappedItemStack;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface DataComponentHandler<T, Z> {

    default void apply(WrappedItemStack stack, DataComponentType<@NotNull Z> type, T data) {
        ItemStack wrapped = ((com.artillexstudios.axapi.nms.v1_21_R7_paper.items.WrappedItemStack) stack).itemStack;
        if (data == null) {
            wrapped.remove(type);
        } else {
            wrapped.set(type, this.toNMS(data));
        }
    }

    default T getData(WrappedItemStack stack, DataComponentType<@NotNull Z> type) {
        ItemStack wrapped = ((com.artillexstudios.axapi.nms.v1_21_R7_paper.items.WrappedItemStack) stack).itemStack;
        Z data = wrapped.get(type);
        if (data == null) {
            return null;
        }

        return this.fromNMS(data);
    }

    Z toNMS(T from);

    T fromNMS(Z data);
}
