package com.artillexstudios.axapi.items.component;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.UUID;

public class ProfileProperties {
    private final Multimap<String, Property> properties = LinkedHashMultimap.create();
    private final UUID uuid;
    private final String name;

    public ProfileProperties(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public void put(String key, Property property) {
        this.properties.put(key, property);
    }

    public Multimap<String, Property> properties() {
        return properties;
    }

    public record Property(String name, String value, String signature) {
    }
}
