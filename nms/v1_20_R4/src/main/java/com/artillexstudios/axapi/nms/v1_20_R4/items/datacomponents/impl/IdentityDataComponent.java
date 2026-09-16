package com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl;

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
