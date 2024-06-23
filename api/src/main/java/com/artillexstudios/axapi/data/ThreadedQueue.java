package com.artillexstudios.axapi.data;

import com.google.common.collect.Queues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadedQueue<T extends Runnable> implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ThreadedQueue.class);
    private final Queue<T> jobs = Queues.newArrayDeque();
    private final Thread thread;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private volatile boolean killed = false;

    public ThreadedQueue(String threadName) {
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

    public void submit(T task) {
        lock.lock();
        try {
            jobs.offer(task);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        while (!killed) {
            try {
                T next = next();

                if (next != null) {
                    next.run();
                }
            } catch (InterruptedException exception) {
                log.error("An unexpected error occurred while running ThreadedQueue {}!", thread.getName(), exception);
            }
        }
    }

    public T next() throws InterruptedException {
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
