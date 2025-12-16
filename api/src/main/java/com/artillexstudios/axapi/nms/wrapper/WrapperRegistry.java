package com.artillexstudios.axapi.nms.wrapper;

import com.artillexstudios.axapi.items.WrappedItemStack;
import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

public class WrapperRegistry {
    public static final WrapperMapper<ServerPlayerWrapper> SERVER_PLAYER = cachingMapper("server_player", FeatureFlags.SERVER_PLAYER_CACHE_SIZE.get());
    public static final WrapperMapper<ServerWrapper> SERVER = mapper("server");
    public static final WrapperMapper<WorldWrapper> WORLD = mapper("world");
    public static final WrapperMapper<WrappedItemStack> ITEM_STACK = mapper("item_stack");

    public static <T extends Wrapper<?>> WrapperMapper<T> mapper(String id) {
        return NMSHandlers.getNmsHandler().mapper(id);
    }

    public static <T extends Wrapper<?>> WrapperMapper<T> cachingMapper(String id, int maximumSize) {
        return new WrapperMapper<>() {
            final Cache<Object, T> mapperCache = Caffeine.newBuilder()
                    .weakKeys()
                    .maximumSize(maximumSize)
                    .expireAfterAccess(Duration.ofSeconds(30))
                    .build();
            final WrapperMapper<T> delegate = mapper(id);

            @Override
            public T map(Object object) {
                return this.mapperCache.get(object, this.delegate::map);
            }
        };
    }
}
