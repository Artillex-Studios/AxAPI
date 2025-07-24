package com.artillexstudios.axapi.executor;

import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

public class ExceptionReportingScheduledThreadPool extends ScheduledThreadPoolExecutor {

    public ExceptionReportingScheduledThreadPool(int corePoolSize) {
        super(corePoolSize);
    }

    public ExceptionReportingScheduledThreadPool(int corePoolSize, ThreadFactory factory) {
        super(corePoolSize, factory);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (r instanceof Future<?> future) {
            if (future.isDone()) {
                try {
                    future.get();
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                } catch (ExecutionException exception) {
                    LogUtils.error("An uncaught exception occurred on thread {}!", Thread.currentThread().getName(), exception.getCause());
                } catch (CancellationException ignored) {
                    // Ignore CancellationException
                }
            }
        }
    }
}
