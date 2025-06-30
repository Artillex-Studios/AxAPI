package com.artillexstudios.axapi.gui.inventory.provider;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.items.WrappedItemStack;

public interface ItemStackProvider<T> {

    WrappedItemStack provide(T data, HashMapContext context);
}
