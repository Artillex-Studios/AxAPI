package com.artillexstudios.axapi.collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class ThreadSafeList<E> extends ArrayList<E> {
    private static final Logger log = LoggerFactory.getLogger(ThreadSafeList.class);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock read = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock write = lock.writeLock();

    @Override
    public int indexOf(Object o) {
        read.lock();
        try {
            return super.indexOf(o);
        } finally {
            read.unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        read.lock();
        try {
            return super.lastIndexOf(o);
        } finally {
            read.unlock();
        }
    }

    @Override
    public void add(int index, E element) {
        write.lock();
        try {
            super.add(index, element);
        } finally {
            write.unlock();
        }
    }

    @Override
    public E get(int index) {
        read.lock();
        try {
            return super.get(index);
        } finally {
            read.unlock();
        }
    }

    @Override
    public E set(int index, E element) {
        write.lock();
        try {
            return super.set(index, element);
        } finally {
            write.unlock();
        }
    }

    @Override
    public E remove(int index) {
        write.lock();
        try {
            return super.remove(index);
        } finally {
            write.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        write.lock();
        try {
            return super.remove(o);
        } finally {
            write.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        write.lock();
        try {
            return super.removeAll(c);
        } finally {
            write.unlock();
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        read.lock();
        try {
            super.forEach(action);
        } finally {
            read.unlock();
        }
    }

    @Override
    public boolean add(E e) {
        write.lock();
        try {
            return super.add(e);
        } finally {
            write.unlock();
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        write.lock();
        try {
            super.sort(c);
        } finally {
            write.unlock();
        }
    }

    @Override
    @Deprecated
    public ListIterator<E> listIterator() {
        UnsupportedOperationException exception = new UnsupportedOperationException("The iterator should not be used!");
        log.warn("Unsupported operation! You should not be using any iterator from the ThreadSafeList! Advanced for loops use the iterator!", exception);
        return super.listIterator();
    }

    @Override
    @Deprecated
    public ListIterator<E> listIterator(int index) {
        UnsupportedOperationException exception = new UnsupportedOperationException("The iterator should not be used!");
        log.warn("Unsupported operation! You should not be using any iterator from the ThreadSafeList! Advanced for loops use the iterator!", exception);
        return super.listIterator(index);
    }

    @Override
    @Deprecated
    public Iterator<E> iterator() {
        UnsupportedOperationException exception = new UnsupportedOperationException("The iterator should not be used!");
        log.warn("Unsupported operation! You should not be using any iterator from the ThreadSafeList! Advanced for loops use the iterator!", exception);
        return super.iterator();
    }
}
