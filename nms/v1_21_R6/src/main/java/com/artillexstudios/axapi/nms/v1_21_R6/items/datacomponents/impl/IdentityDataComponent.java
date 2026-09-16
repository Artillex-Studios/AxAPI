package com.artillexstudios.axapi.nms.v1_21_R6.items.datacomponents.impl;

import com.artillexstudios.axapi.nms.v1_21_R6.items.datacomponents.impl.DataComponentHandler;

public final class IdentityDataComponent<T> implements DataComponentHandler<T, T> {

    @Override
    public T toNMS(T from) {
        return from;
    }

    @Override
    public T fromNMS(T data) {
        return data;
    }
}
