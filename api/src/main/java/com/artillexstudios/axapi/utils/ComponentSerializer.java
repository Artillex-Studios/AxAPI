package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.nms.NMSHandlers;
import com.artillexstudios.axapi.serializers.Serializer;
import com.google.gson.stream.MalformedJsonException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public enum ComponentSerializer {
    INSTANCE;

    private final Serializer<Object, Component> serializer = NMSHandlers.getNmsHandler().componentSerializer();

    public <T> T toVanilla(Component component) {
        return (T) serializer.deserialize(component);
    }

    public Component fromVanilla(Object object) {
        return serializer.serialize(object);
    }

    public <T> List<T> toVanillaList(List<Component> components) {
        List<T> converted = new ArrayList<>(components.size());
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            converted.add(toVanilla(component));
        }

        return converted;
    }

    public List<Component> fromVanillaList(List<Object> objects) {
        List<Component> converted = new ArrayList<>(objects.size());
        for (int i = 0; i < objects.size(); i++) {
            Object component = objects.get(i);
            converted.add(fromVanilla(component));
        }

        return converted;
    }

    public ArrayList<Component> asAdventureFromJson(List<String> jsonStrings) {
        ArrayList<Component> adventures = new ArrayList<>(jsonStrings.size());
        Iterator<String> var2 = jsonStrings.iterator();

        while (var2.hasNext()) {
            String json = var2.next();

            try {
                adventures.add(GsonComponentSerializer.gson().deserialize(json));
            } catch (Exception exception) {
                adventures.add(Component.empty());
            }
        }

        return adventures;
    }

    public String toGson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }
}
