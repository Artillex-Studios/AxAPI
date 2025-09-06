package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@NullMarked
public final class ExceptionUtils {

    @Nullable
    public static <T> T catching(Supplier<T> supplier) {
        return catching(supplier, exception -> {
            if (FeatureFlags.LOG_EXCEPTION_UTILS_EXCEPTIONS.get()) {
                LogUtils.error("ExceptionUtils caught exception!", exception);
            }

            return null;
        });
    }

    @Nullable
    public static <T> T catching(Supplier<T> supplier, Function<Exception, @Nullable T> function) {
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
