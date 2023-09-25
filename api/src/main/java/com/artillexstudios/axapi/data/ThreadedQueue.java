package com.artillexstudios.axapi.data;

import com.google.common.collect.Queues;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadedQueue<T extends Runnable> implements Runnable {
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

        synchronized (lock) {
            condition.signalAll();
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
            } catch (Exception exception) {
                exception.printStackTrace();
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
