package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.util.function.Supplier;

public final class ExceptionUtils {

    public static <T> T catching(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception exception) {
            if (FeatureFlags.LOG_EXCEPTION_UTILS_EXCEPTIONS.get()) {
                LogUtils.error("ExceptionUtils caught exception!", exception);
            }

            return null;
        }
    }

    public static void catching(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception exception) {
            if (FeatureFlags.LOG_EXCEPTION_UTILS_EXCEPTIONS.get()) {
                LogUtils.error("ExceptionUtils caught exception!", exception);
            }
        }
    }
}
