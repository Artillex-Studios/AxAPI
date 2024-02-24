package com.artillexstudios.axapi.utils;

import com.google.common.base.Preconditions;

public class FeatureFlags {
    public static Flag<Boolean> PACKET_ENTITY_TRACKER_ENABLED = new Flag<>(false);
    public static Flag<Integer> PACKET_ENTITY_TRACKER_THREADS = new Flag<>(3);

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
