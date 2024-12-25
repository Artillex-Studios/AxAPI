package com.artillexstudios.axapi.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Registry<K, V> {
    private final ConcurrentHashMap<K, V> entries = new ConcurrentHashMap<>();
    private final Set<K> keys = Collections.unmodifiableSet(this.entries.keySet());
    private final Collection<V> values = Collections.unmodifiableCollection(this.entries.values());

    public void register(K key, V value) throws RegistrationFailedException {
        if (this.entries.containsKey(key)) {
            throw new RegistrationFailedException(key, RegistrationFailedException.Cause.ALREADY_PRESENT);
        }

        this.entries.put(key, value);
    }

    public void deregister(K key) throws RegistrationFailedException {
        if (!this.entries.containsKey(key)) {
            throw new RegistrationFailedException(key, RegistrationFailedException.Cause.NOT_PRESENT);
        }

        this.entries.remove(key);
    }

    public V get(K key) throws RegistrationFailedException {
        if (!this.entries.containsKey(key)) {
            throw new RegistrationFailedException(key, RegistrationFailedException.Cause.NOT_PRESENT);
        }

        return this.entries.get(key);
    }

    public Set<K> keys() {
        return this.keys;
    }

    public Collection<V> values() {
        return this.values;
    }
}
