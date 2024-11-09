package com.artillexstudios.axapi.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class IdentityArrayMap<K, V> implements Map<K, V> {
    private static final Object[] EMPTY_ARRAY = new Object[0];
    private Object[] keys;
    private Object[] values;
    private int size;

    public IdentityArrayMap() {
        this.keys = EMPTY_ARRAY;
        this.values = EMPTY_ARRAY;
    }

    public IdentityArrayMap(int size) {
        this.keys = new Object[size];
        this.values = new Object[size];
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.findKey(key) != -1;
    }

    @Override
    public boolean containsValue(Object value) {
        Object[] values = this.values;
        int i = this.size;

        while (values[i] != value) {
            if (i-- == 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public V get(Object key) {
        int index = this.findKey(key);
        return index == -1 ? null : (V) this.values[index];
    }

    @Override
    public V put(K key, V value) {
        int previousPos = this.findKey(key);
        if (previousPos != -1) {
            V previous = (V) this.values[previousPos];
            this.values[previousPos] = value;
            return previous;
        } else {
            if (this.size == this.keys.length) {
                Object[] newKeys = new Object[this.size == 0 ? 2 : this.size * 2];
                Object[] newValues = new Object[this.size == 0 ? 2 : this.size * 2];

                System.arraycopy(this.keys, 0, newKeys, 0, this.size);
                System.arraycopy(this.values, 0, newValues, 0, this.size);

                this.keys = newKeys;
                this.values = newValues;
            }

            this.keys[this.size] = key;
            this.values[this.size] = value;
            this.size++;
            return null;
        }
    }

    @Override
    public V remove(Object key) {
        int previousPos = this.findKey(key);
        if (previousPos == -1) {
            return null;
        }

        V oldValue = (V) this.values[previousPos];
        int tail = this.size - previousPos - 1;
        System.arraycopy(this.keys, previousPos + 1, this.keys, previousPos, tail);
        System.arraycopy(this.values, previousPos + 1, this.values, previousPos, tail);
        this.size--;
        this.keys[this.size] = null;
        this.values[this.size] = null;
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.size; i++) {
            this.keys[i] = null;
            this.values[i] = null;
        }

        this.size = 0;
    }

    @Override
    public Set<K> keySet() {
        return (Set<K>) Set.of(this.keys);
    }

    @Override
    public Collection<V> values() {
        return (Collection<V>) List.of(this.values);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<Entry<K, V>> iterator() {
                return new Iterator<>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return this.index < IdentityArrayMap.this.size;
                    }

                    @Override
                    public Entry<K, V> next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }

                        return new Entry<>() {
                            private final int currentIndex = index++;

                            @Override
                            public K getKey() {
                                return (K) IdentityArrayMap.this.keys[currentIndex];
                            }

                            @Override
                            public V getValue() {
                                return (V) IdentityArrayMap.this.values[currentIndex];
                            }

                            @Override
                            public V setValue(V value) {
                                V old = (V) IdentityArrayMap.this.values[currentIndex];
                                IdentityArrayMap.this.values[currentIndex] = value;
                                return old;
                            }
                        };
                    }

                    @Override
                    public void remove() {
                        if (this.index < 0) {
                            throw new IllegalStateException();
                        }

                        IdentityArrayMap.this.remove(IdentityArrayMap.this.keys[this.index - 1]);
                        this.index--;
                    }
                };
            }


            @Override
            public int size() {
                return IdentityArrayMap.this.size;
            }
        };
    }

    private int findKey(Object key) {
        Object[] keys = this.keys;
        int i = this.size;

        if (i == keys.length) {
            return -1;
        }

        while (keys[i] != key) {
            if (i-- == 0) {
                return -1;
            }
        }

        return i;
    }
}
