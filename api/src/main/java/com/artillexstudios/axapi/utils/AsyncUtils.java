package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.AxPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class AsyncUtils {
    private static final Logger log = LoggerFactory.getLogger(AsyncUtils.class);
    private static ScheduledExecutorService executorService;

    public static void setup(int poolSize) {
        executorService = new ExceptionReportingScheduledThreadPool(Math.max(1, poolSize), new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);

            @Override
            public Thread newThread(@NotNull Runnable runnable) {
                return new Thread(null, runnable, AxPlugin.getPlugin().getName() + "-Async-Processor-Thread-" + counter.getAndIncrement());
            }
        });
    }

    public static void run(Runnable runnable, boolean async) {
        if (async) {
            executorService.submit(runnable);
        } else {
            runnable.run();
        }
    }

    public static Future<?> submit(Runnable runnable, boolean async) {
        if (async) {
            return executorService.submit(runnable);
        }

        CompletableFuture<?> future = new CompletableFuture<>();
        runnable.run();
        future.complete(null);
        return future;
    }

    public static Future<?> submit(Runnable runnable) {
        return executorService.submit(runnable);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long initDelay, long period, TimeUnit timeUnit) {
        return executorService.scheduleAtFixedRate(runnable, initDelay, period, timeUnit);
    }

    public static ScheduledFuture<?> runLater(Runnable runnable, long delay, TimeUnit timeUnit) {
        return executorService.schedule(runnable, delay, timeUnit);
    }

    public static ScheduledExecutorService executor() {
        return executorService;
    }

    public static void stop() {
        try {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException exception) {
            log.error("An unexpected error occurred while stopping AsyncUtils!", exception);
        }
    }
}
