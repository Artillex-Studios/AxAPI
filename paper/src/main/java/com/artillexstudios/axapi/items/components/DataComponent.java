package com.artillexstudios.axapi.items.components;

import com.artillexstudios.axapi.items.WrappedItemStack;

public interface DataComponent<T> {

    void apply(WrappedItemStack stack, T data);

    T getData(WrappedItemStack stack);
}
