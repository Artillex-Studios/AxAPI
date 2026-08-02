package com.artillexstudios.axapi.nms.v1_21_R4.items.datacomponents.impl;

import com.artillexstudios.axapi.items.WrappedItemStack;
import net.minecraft.core.component.DataComponentType;
import org.jetbrains.annotations.NotNull;

public final class DataComponent {

    public static <T, Z> com.artillexstudios.axapi.items.components.DataComponent<T> create(String id, DataComponentType<@NotNull Z> type, DataComponentHandler<T, Z> handler) {
        return new com.artillexstudios.axapi.items.components.DataComponent<>() {

            @Override
            public void apply(WrappedItemStack stack, T data) {
                handler.apply(stack, type, data);
            }

            @Override
            public T getData(WrappedItemStack stack) {
                return handler.getData(stack, type);
            }

            @Override
            public String getId() {
                return id;
            }
        };
    }
}
