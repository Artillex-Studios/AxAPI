package com.artillexstudios.axapi.utils;

import java.util.Collection;
import java.util.function.Function;

public class MathUtils {

    public static <T> int findLargestValue(Collection<T> collection, Function<T, Integer> function) {
        int max = Integer.MIN_VALUE;
        for (T t : collection) {
            max = Math.max(max, function.apply(t));
        }

        return max;
    }
}
