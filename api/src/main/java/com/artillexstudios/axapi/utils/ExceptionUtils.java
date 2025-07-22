package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ExceptionUtils {

    public static <T> T catching(Supplier<T> supplier) {
        return catching(supplier, exception -> {
            if (FeatureFlags.LOG_EXCEPTION_UTILS_EXCEPTIONS.get()) {
                LogUtils.error("ExceptionUtils caught exception!", exception);
            }

            return null;
        });
    }

    public static <T> T catching(Supplier<T> supplier, Function<Exception, T> function) {
        try {
            return supplier.get();
        } catch (Exception exception) {
            return function.apply(exception);
        }
    }

    public static void catching(Runnable runnable) {
        catching(runnable, exception -> {
            if (FeatureFlags.LOG_EXCEPTION_UTILS_EXCEPTIONS.get()) {
                LogUtils.error("ExceptionUtils caught exception!", exception);
            }
        });
    }

    public static void catching(Runnable runnable, Consumer<Exception> consumer) {
        try {
            runnable.run();
        } catch (Exception exception) {
            consumer.accept(exception);
        }
    }
}
