package com.artillexstudios.axapi.data;

import com.google.common.collect.Queues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An implementation of ThreadedQueue which is also an executor.
 * This can be used in CompletableFutures, or other places aswell.
 * This class also has a method that returns a future.
 */
public class ThreadedExecutor implements Runnable, Executor {
    private static final Logger log = LoggerFactory.getLogger(ThreadedExecutor.class);
    private final Queue<Runnable> jobs = Queues.newArrayDeque();
    private final Thread thread;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private volatile boolean killed = false;

    public ThreadedExecutor(String threadName) {
        this.thread = new Thread(this, threadName);
        thread.start();
    }

    public void stop() {
        killed = true;

        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void execute(Runnable task) {
        lock.lock();
        try {
            jobs.offer(task);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public CompletableFuture<Void> submit(Runnable task) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        execute(() -> {
            try {
                task.run();
            } catch (Throwable throwable) {
                future.completeExceptionally(throwable);
                return;
            }

            future.complete(null);
        });

        return future;
    }

    @Override
    public void run() {
        while (!killed) {
            try {
                Runnable next = next();

                if (next != null) {
                    next.run();
                }
            } catch (Exception exception) {
                log.error("An unexpected error occurred while running ThreadedExecutor {}!", thread.getName(), exception);
            }
        }
    }

    public Runnable next() throws InterruptedException {
        lock.lock();
        try {
            while (jobs.isEmpty() && !killed) {
                condition.await();
            }

            if (jobs.isEmpty()) {
                return null;
            }

            return jobs.remove();
        } finally {
            lock.unlock();
        }
    }
}
