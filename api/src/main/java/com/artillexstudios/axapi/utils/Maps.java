package com.artillexstudios.axapi.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class Maps {

    public static <T, Z> Map<T, Z> orderByValue(Map<T, Z> map) {
        return (Map<T, Z>) map.entrySet()
                .stream()
                .sorted((java.util.Comparator) Map.Entry.comparingByValue())
                .collect(Collectors.toMap(entry -> ((Map.Entry<T, Z>) entry).getKey(), entry -> ((Map.Entry<T, Z>) entry).getValue(), (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static <T, Z> Map<T, Z> order(Map<T, Z> map) {
        return (Map<T, Z>) map.entrySet()
                .stream()
                .sorted((java.util.Comparator) Map.Entry.comparingByKey())
                .collect(Collectors.toMap(entry -> ((Map.Entry<T, Z>) entry).getKey(), entry -> ((Map.Entry<T, Z>) entry).getValue(), (e1, e2) -> e1, LinkedHashMap::new));
    }
}
