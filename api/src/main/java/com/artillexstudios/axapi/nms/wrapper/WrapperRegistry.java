package com.artillexstudios.axapi.nms.wrapper;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

public class WrapperRegistry {
    public static final WrapperMapper<ServerPlayerWrapper> SERVER_PLAYER = cachingMapper("server_player");
    public static final WrapperMapper<ServerWrapper> SERVER = mapper("server");
    public static final WrapperMapper<WorldWrapper> WORLD = mapper("world");
    public static final WrapperMapper<WrappedItemStack> ITEM_STACK = mapper("item_stack");

    public static <T extends Wrapper<?>> WrapperMapper<T> mapper(String id) {
        return NMSHandlers.getNmsHandler().mapper(id);
    }

    public static <T extends Wrapper<?>> WrapperMapper<T> cachingMapper(String id) {
        return new WrapperMapper<>() {
            final Cache<Object, T> mapperCache = Caffeine.newBuilder()
                    .maximumSize(50)
                    .expireAfterAccess(Duration.ofSeconds(20))
                    .build();
            final WrapperMapper<T> delegate = mapper(id);

            @Override
            public T map(Object object) {
                return this.mapperCache.get(object, this.delegate::map);
            }
        };
    }
}
