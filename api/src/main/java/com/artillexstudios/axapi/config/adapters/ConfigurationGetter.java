package com.artillexstudios.axapi.config.adapters;

import com.artillexstudios.axapi.utils.UncheckedUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

public interface ConfigurationGetter {

    <T> T get(String path, Class<T> clazz);

    default Object getObject(String path) {
        return this.get(path, Object.class);
    }

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

    default <T> List<T> getList(String path) {
        return UncheckedUtils.unsafeCast(this.get(path, List.class));
    }

    default <T, Z> List<T> getList(String path, Function<Z, T> converter) {
        List<Z> list = UncheckedUtils.unsafeCast(this.get(path, List.class));
        List<T> newList = new ArrayList<>(list.size());
        for (Z object : list) {
            newList.add(converter.apply(object));
        }
        return newList;
    }

    default <T, Z> Map<T, Z> getMap(String path) {
        return UncheckedUtils.unsafeCast(this.get(path, Map.class));
    }

    default MapConfigurationGetter getConfiguration(String path) {
        return new MapConfigurationGetter(UncheckedUtils.unsafeCast(this.get(path, Map.class)));
    }

    default MapConfigurationGetter getConfigurationSection(String path) {
        return new MapConfigurationGetter(UncheckedUtils.unsafeCast(this.get(path, LinkedHashMap.class)));
    }

    default <T, Z> Map<T, Z> getSection(String path) {
        return UncheckedUtils.unsafeCast(this.get(path, LinkedHashMap.class));
    }

    default UUID getUUID(String path) {
        return this.get(path, UUID.class);
    }

    default Long getLong(String path) {
        return this.get(path, Long.class);
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

    default <T extends Enum<T>> T getEnum(String path, Class<T> clazz) {
        return this.get(path, clazz);
    }
}
