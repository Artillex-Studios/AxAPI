package com.artillexstudios.axapi.collections;

import com.artillexstudios.axapi.utils.mutable.MutableInteger;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a locking queue where nodes are cached for a
 * small memory footprint.
 * @param <E> The type parameter of this queue.
 */
public class NodeCachingLockingQueue<E> {
    private static final class Node<E> {
        E item;
        Node<E> next;
    }

    private final Node<E> head = new Node<>();
    private Node<E> tail;
    private final ArrayDeque<Node<E>> nodeCache = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final MutableInteger size = new MutableInteger();

    public NodeCachingLockingQueue() {
        this(0);
    }

    public NodeCachingLockingQueue(int cacheSize) {
        for (int i = 0; i < cacheSize; i++) {
            this.nodeCache.add(new Node<>());
        }
        this.tail = this.head;
    }

    private Node<E> getOrCreateNode() {
        Node<E> node = this.nodeCache.pollFirst();
        return node == null ? new Node<>() : node;
    }

    private void releaseNode(Node<E> node) {
        node.item = null;
        node.next = null;
        this.nodeCache.addFirst(node);
        this.size.decrementAndGet();
    }

    public void addAll(Collection<E> collection) {
        this.lock.lock();
        try {
            for (E element : collection) {
                Node<E> node = this.getOrCreateNode();
                node.item = element;
                this.tail.next = node;
                this.tail = node;
                this.size.incrementAndGet();
            }
        } finally {
            this.lock.unlock();
        }
    }

    public void offer(E item) {
        this.lock.lock();
        try {
            Node<E> node = this.getOrCreateNode();
            node.item = item;
            this.tail.next = node;
            this.tail = node;
            this.size.incrementAndGet();
        } finally {
            this.lock.unlock();
        }
    }

    public E poll() {
        this.lock.lock();
        try {
            Node<E> first = this.head.next;
            if (first == null) {
                return null;
            }

            E item = first.item;
            this.head.next = first.next;
            if (this.tail == first) {
                this.tail = this.head;
            }

            this.releaseNode(first);
            return item;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean isEmpty() {
        return this.head.next == null;
    }

    public int size() {
        return this.size.intValue();
    }
}
