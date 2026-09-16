package com.artillexstudios.axapi.nms.v1_20_R3.items.datacomponents.impl;

import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class DirectDataComponent<T> implements DataComponentHandler<T> {
    private final Function<ItemStack, T> getter;
    private final BiConsumer<ItemStack, T> setter;
    private final Consumer<ItemStack> remover;

    public DirectDataComponent(Function<ItemStack, T> getter, BiConsumer<ItemStack, T> setter, Consumer<ItemStack> remover) {
        this.getter = getter;
        this.setter = setter;
        this.remover = remover;
    }

    @Override
    public void setData(ItemStack from, T data) {
        this.setter.accept(from, data);
    }

    @Override
    public T getData(ItemStack stack) {
        return this.getter.apply(stack);
    }

    @Override
    public void removeData(ItemStack itemStack) {
        this.remover.accept(itemStack);
    }
}
