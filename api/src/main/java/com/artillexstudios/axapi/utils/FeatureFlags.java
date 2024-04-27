package com.artillexstudios.axapi.utils;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class FeatureFlags {
    public static final Flag<Boolean> PACKET_ENTITY_TRACKER_ENABLED = new Flag<>(false);
    public static final Flag<Boolean> DEBUG = new Flag<>(false);
    public static final Flag<Integer> PACKET_ENTITY_TRACKER_THREADS = new Flag<>(3);
    public static final Flag<Long> HOLOGRAM_UPDATE_TICKS = new Flag<>(0L);
    public static final Flag<List<Pattern>> PLACEHOLDER_PATTERNS = new Flag<>(new ArrayList<>(Arrays.asList(Pattern.compile("%.+%"), Pattern.compile("<.+>"))));

    public static class Flag<T> {
        private T value;

        public Flag(T value) {
            this.value = value;
        }

        public void set(T value) {
            Preconditions.checkNotNull(value, "Can not set a flag to null!");
            this.value = value;
        }

        public T get() {
            return value;
        }
    }
}
