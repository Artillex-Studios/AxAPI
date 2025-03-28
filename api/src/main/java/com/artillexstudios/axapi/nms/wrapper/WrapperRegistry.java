package com.artillexstudios.axapi.nms.wrapper;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.nms.NMSHandlers;

public class WrapperRegistry {
    public static final WrapperMapper<ServerPlayerWrapper> SERVER_PLAYER = mapper("server_player");
    public static final WrapperMapper<ServerWrapper> SERVER = mapper("server");
    public static final WrapperMapper<WorldWrapper> WORLD = mapper("world");
    public static final WrapperMapper<WrappedItemStack> ITEM_STACK = mapper("item_stack");

    public static <T extends WrapperMapper<?>> T mapper(String id) {
        return NMSHandlers.getNmsHandler().mapper(id);
    }
}
