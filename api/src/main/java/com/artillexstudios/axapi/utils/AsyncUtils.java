package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.AxPlugin;
import com.artillexstudios.axapi.executor.ExceptionReportingScheduledThreadPool;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class AsyncUtils {

    private static final class Holder {
        private static final ScheduledExecutorService EXECUTOR = new ExceptionReportingScheduledThreadPool(Math.max(1, FeatureFlags.ASYNC_UTILS_POOL_SIZE.get()), new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);

            @Override
            public Thread newThread(@NotNull Runnable runnable) {
                return new Thread(null, runnable, AxPlugin.getPlugin(AxPlugin.class).getName() + "-Async-Processor-Thread-" + this.counter.getAndIncrement());
            }
        });
    }

    public static void run(Runnable runnable, boolean async) {
        if (async) {
            Holder.EXECUTOR.submit(runnable);
        } else {
            runnable.run();
        }
    }

    public static Future<?> submit(Runnable runnable, boolean async) {
        if (async) {
            return Holder.EXECUTOR.submit(runnable);
        }

        CompletableFuture<?> future = new CompletableFuture<>();
        runnable.run();
        future.complete(null);
        return future;
    }

    public static Future<?> submit(Runnable runnable) {
        return Holder.EXECUTOR.submit(runnable);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initDelay, long period, TimeUnit timeUnit) {
        return Holder.EXECUTOR.scheduleAtFixedRate(runnable, initDelay, period, timeUnit);
    }

    public static ScheduledFuture<?> runLater(Runnable runnable, long delay, TimeUnit timeUnit) {
        return Holder.EXECUTOR.schedule(runnable, delay, timeUnit);
    }

    public static ScheduledExecutorService executor() {
        return Holder.EXECUTOR;
    }

    public static void stop() {
        try {
            Holder.EXECUTOR.shutdown();
            Holder.EXECUTOR.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException exception) {
            LogUtils.error("An unexpected error occurred while stopping AsyncUtils!", exception);
        }
    }
}
