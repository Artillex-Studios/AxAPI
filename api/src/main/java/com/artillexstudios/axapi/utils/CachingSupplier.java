package com.artillexstudios.axapi.utils;

import java.util.function.Supplier;

public interface CachingSupplier<T> extends Supplier<T> {

    static <Z> Supplier<Z> create(Supplier<Z> supplier) {
        return new Supplier<>() {
            private Z value;

            @Override
            public Z get() {
                if (this.value == null) {
                    this.value = supplier.get();
                }

                return this.value;
            }
        };
    }
}
