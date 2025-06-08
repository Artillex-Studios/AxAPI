package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.serializers.Serializer;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public enum ComponentSerializer {
    INSTANCE;

    private Serializer<Object, Component> serializer;
    private Cache<Component, Object> componentCache;
    private Cache<Object, Component> vanillaCache;
    private Cache<String, Component> gsonCache;

    public void refresh() {
        serializer = NMSHandlers.getNmsHandler().componentSerializer();
        this.componentCache = Caffeine.newBuilder()
                .maximumSize(FeatureFlags.COMPONENT_CACHE_SIZE.get())
                .expireAfterAccess(Duration.ofMinutes(5))
                .build();
        this.vanillaCache = Caffeine.newBuilder()
                .maximumSize(FeatureFlags.COMPONENT_CACHE_SIZE.get())
                .expireAfterAccess(Duration.ofMinutes(5))
                .build();
        this.gsonCache = Caffeine.newBuilder()
                .maximumSize(FeatureFlags.COMPONENT_CACHE_SIZE.get())
                .expireAfterAccess(Duration.ofMinutes(5))
                .build();
    }

    public <T> T toVanilla(Component component) {
        return (T) this.componentCache.get(component, this.serializer::deserialize);
    }

    public Component fromVanilla(Object object) {
        return this.vanillaCache.get(object, this.serializer::serialize);
    }

    public <T> List<T> toVanillaList(List<Component> components) {
        List<T> converted = new ArrayList<>(components.size());
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            converted.add(this.toVanilla(component));
        }

        return converted;
    }

    public List<Component> fromVanillaList(List<Object> objects) {
        List<Component> converted = new ArrayList<>(objects.size());
        for (int i = 0; i < objects.size(); i++) {
            Object component = objects.get(i);
            converted.add(this.fromVanilla(component));
        }

        return converted;
    }

    public List<String> toGsonList(List<Component> list) {
        ArrayList<String> newList = new ArrayList<>(list.size());
        for (Component component : list) {
            newList.add(this.toGson(component));
        }

        return newList;
    }

    public String toGson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }

    public Component fromGson(String string) {
        return this.gsonCache.get(string, s -> {
            try {
                return GsonComponentSerializer.gson().deserialize(s);
            } catch (Exception exception) {
                return Component.empty();
            }
        });
    }

    public List<Component> fromGsonList(List<String> list) {
        ArrayList<Component> newList = new ArrayList<>(list.size());
        for (String line : list) {
            newList.add(this.fromGson(line));
        }

        return newList;
    }
}
