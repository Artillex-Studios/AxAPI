package com.artillexstudios.axapi.config.adapters;

import com.artillexstudios.axapi.utils.UncheckedUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public interface ConfigurationGetter {

    <T> T get(String path, Class<T> clazz);

    Set<String> getKeys();

    default <T> T anyOf(Function<String, T> function, String... paths) {
        for (String path : paths) {
            T value = function.apply(path);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

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
        if (list == null) {
            return null;
        }

        List<T> newList = new ArrayList<>(list.size());
        for (Z object : list) {
            if (object == null) {
                continue;
            }
            T apply = converter.apply(object);
            if (apply == null) {
                continue;
            }

            newList.add(apply);
        }
        return newList;
    }

    default <T> T getOrDefault(String path, Function<String, T> function, T def) {
        T result = function.apply(path);
        return result == null ? def : result;
    }

    default List<String> getStringList(String path) {
        return this.getList(path, Object::toString);
    }

    default <T, Z> Map<T, Z> getMap(String path) {
        return UncheckedUtils.unsafeCast(this.get(path, Map.class));
    }

    default <T, Z> List<Map<T, Z>> getMapList(String path) {
        return this.getList(path);
    }

    default List<MapConfigurationGetter> getConfigurationList(String path) {
        return this.getList(path, object -> new MapConfigurationGetter(UncheckedUtils.unsafeCast(object)));
    }

    default MapConfigurationGetter getConfiguration(String path) {
        return new MapConfigurationGetter(UncheckedUtils.unsafeCast(this.get(path, Map.class)));
    }

    default boolean contains(String path) {
        return this.getObject(path) != null;
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
