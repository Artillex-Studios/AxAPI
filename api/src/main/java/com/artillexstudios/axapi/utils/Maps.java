package com.artillexstudios.axapi.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class Maps {

    public static <T, Z> Map<T, Z> orderByValue(Map<T, Z> map) {
        return map.entrySet()
                .stream()
                .sorted(UncheckedUtils.unsafeCast(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static <T, Z> Map<T, Z> order(Map<T, Z> map) {
        return map.entrySet()
                .stream()
                .sorted(UncheckedUtils.unsafeCast(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
