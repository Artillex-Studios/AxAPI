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

    public static class Property {
        private final String name;
        private final String value;
        private final String signature;

        public Property(String name, String value, String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        public String signature() {
            return signature;
        }

        public String value() {
            return value;
        }

        public String name() {
            return name;
        }
    }
}
