package com.artillexstudios.axapi.nms.v1_20_R3.items.datacomponents.impl;

import com.artillexstudios.axapi.items.WrappedItemStack;

public final class DataComponent {

    public static <T> com.artillexstudios.axapi.items.components.DataComponent<T> create(String id, DataComponentHandler<T> handler) {
        return new com.artillexstudios.axapi.items.components.DataComponent<>() {

            @Override
            public void apply(WrappedItemStack stack, T data) {
                handler.apply(stack, data);
            }

            @Override
            public T getData(WrappedItemStack stack) {
                return handler.getData(stack);
            }

            @Override
            public String getId() {
                return id;
            }
        };
    }
}
