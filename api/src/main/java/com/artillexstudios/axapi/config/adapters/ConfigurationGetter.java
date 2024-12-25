package com.artillexstudios.axapi.config.adapters;

import com.artillexstudios.axapi.items.WrappedItemStack;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public interface ConfigurationGetter {

    <T> T get(String path, Class<T> clazz);

    default Boolean getBoolean(String path) {
        return this.get(path, Boolean.class);
    }

    default String getString(String path) {
        return this.get(path, String.class);
    }

    default Double getDouble(String path) {
        return this.get(path, Double.class);
    }

    default Integer getInteger(String path) {
        return this.get(path, Integer.class);
    }

    default Float getFloat(String path) {
        return this.get(path, Float.class);
    }

    default Short getShort(String path) {
        return this.get(path, Short.class);
    }

    default Byte getByte(String path) {
        return this.get(path, Byte.class);
    }

    default List<?> getList(String path) {
        return this.get(path, List.class);
    }

    default Map<?, ?> getMap(String path) {
        return this.get(path, Map.class);
    }

    default Map<?, ?> getSection(String path) {
        return this.get(path, LinkedHashMap.class);
    }

    default UUID getUUID(String path) {
        return this.get(path, UUID.class);
    }

    default Long getLong(String path) {
        return this.get(path, Long.class);
    }

    default WrappedItemStack getWrappedItemStack(String path) {
        return this.get(path, WrappedItemStack.class);
    }

    default BigDecimal getBigDecimal(String path) {
        return this.get(path, BigDecimal.class);
    }

    default BigInteger getBigInteger(String path) {
        return this.get(path, BigInteger.class);
    }

    default Pattern getPattern(String path) {
        return this.get(path, Pattern.class);
    }

    default ItemStack getItemStack(String path) {
        return this.get(path, ItemStack.class);
    }

    default <T extends Enum<T>> T getEnum(String path, Class<T> clazz) {
        return this.get(path, clazz);
    }
}
